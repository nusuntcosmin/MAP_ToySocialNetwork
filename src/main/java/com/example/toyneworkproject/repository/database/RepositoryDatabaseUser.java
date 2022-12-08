package com.example.toyneworkproject.repository.database;

import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.exceptions.RepositoryException;
import com.example.toyneworkproject.repository.Repository;
import com.example.toyneworkproject.repository.memory.MemoryRepository;

import java.sql.*;

import java.util.HashSet;
import java.util.UUID;

public class RepositoryDatabaseUser implements Repository<UUID, User> {

    private String URL;
    private String username;
    private String password;

    private final String SAVE_USER_SQL = "INSERT INTO users(\"firstName\",\"lastName\",\"userUUID\",\"email\",\"nanoSecondsOnline\")" +
            " VALUES(?, ?, ?, ?, ?);";

    private final String GET_ALL_USERS_SQL = "SELECT * FROM users";

    private final String GET_USER_SQL = "SELECT * FROM users " +
                                        "WHERE \"userUUID\" = ? ;" ;

    private final String DELETE_USER_SQL ="DELETE FROM users \n" +
            "WHERE \"userUUID\" = ? ;";

    private final String UPDATE_USER_SQL ="UPDATE users set \"firstName\" = ? , \"lastName\" = ? , \"email\" = ?, \"nanoSecondsOnline\" = ? " +
                                          "\nWHERE \"userUUID\" = ? ;";

    public RepositoryDatabaseUser(String URL, String username, String password) {
        this.URL = URL;
        this.username = username;
        this.password = password;


    }

    @Override
    public User delete(UUID uuid) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(DELETE_USER_SQL)) {
                ps.setObject(1,uuid);
                ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("An user with that ID could not be found");
        }
        return null;
    }

    @Override
    public User findOne(UUID uuid) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = connection.prepareStatement(GET_USER_SQL)){
            statement.setObject(1,uuid);
             ResultSet resultSet = statement.executeQuery();

             while(resultSet.next()) {
                 UUID userUUID = (UUID) resultSet.getObject("userUUID");
                 String firstName = resultSet.getString("firstName");
                 String lastName = resultSet.getString("lastName");
                 String email = resultSet.getString("email");
                 long nanoSecondsOnline = resultSet.getLong("nanoSecondsOnline");
                 User userFound = new User(firstName, lastName, email);
                 userFound.setNanoSecondsOnline(nanoSecondsOnline);

                 userFound.setUserID(userUUID);

                 return userFound;
             }

        } catch (SQLException e) {
            throw new RepositoryException("User cannot be found");
        }
        return null;
    }


    @Override
    public Iterable<User> findAll() {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_USERS_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            HashSet<User> users = new HashSet<>();

            while (resultSet.next()) {
                UUID userUUID = (UUID) resultSet.getObject("userUUID");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                long nanoSecondsOnline = resultSet.getLong("nanoSecondsOnline");
                User user = new User(firstName, lastName,email);
                user.setNanoSecondsOnline(nanoSecondsOnline);
                user.setUserID(userUUID);
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Probleme baza date");
        }

    }

    @Override
    public User update(User entity) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(UPDATE_USER_SQL)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setObject(5, entity.getUserID());
            ps.setLong(4,entity.getNanoSecondsOnline());

            ps.executeUpdate();

            return entity;

        } catch (SQLException e) {
            throw new RepositoryException("Problems ocurred when trying to update the user");
        }

    }

    @Override
    public User save(User entity) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(SAVE_USER_SQL)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(4, entity.getEmail());
            ps.setObject(3, entity.getUserID());
            ps.setLong(5, entity.getNanoSecondsOnline());

            ps.executeUpdate();

            return entity;
        } catch (SQLException e) {
            throw new RepositoryException("Problems ocurred when trying to save the user");
        }

    }
}
