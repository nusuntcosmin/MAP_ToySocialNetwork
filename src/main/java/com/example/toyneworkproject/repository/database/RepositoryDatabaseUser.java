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

    private final String SAVE_USER_SQL = "INSERT INTO users(\"firstName\",\"lastName\",\"userUUID\",\"email\")" +
            " VALUES(?, ?, ?, ?);";

    private final String GET_ALL_USERS_SQL = "SELECT * FROM users";

    private final String GET_USER_SQL = "SELECT * FROM users " +
                                        "WHERE \"userUUID\" = ? ;" ;

    private final String DELETE_USER_SQL ="DELETE FROM users \n" +
            "WHERE \"userUUID\" = ? ;";

    private final String UPDATE_USER_SQL ="UPDATE users set \"firstName\" = ? , \"lastName\" = ? , \"email\" = ? " +
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
             PreparedStatement statement = connection.prepareStatement(GET_USER_SQL);
             ResultSet resultSet = statement.executeQuery()) {
                 UUID userUUID = (UUID) resultSet.getObject("userUUID");
                 String firstName = resultSet.getString("firstName");
                 String lastName = resultSet.getString("lastName");
                 String email = resultSet.getString("email");
                 User userFound = new User(firstName, lastName,email);

                 return userFound;

        } catch (SQLException e) {
            throw new RepositoryException("User cannot be found");
        }
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
                User user = new User(firstName, lastName,email);
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
            ps.setObject(4, entity.getUserID());

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

            ps.executeUpdate();

            return entity;
        } catch (SQLException e) {
            throw new RepositoryException("Problems ocurred when trying to save the user");
        }

    }
}
