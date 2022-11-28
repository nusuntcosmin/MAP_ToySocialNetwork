package com.example.toyneworkproject.repository.database;

import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.repository.memory.MemoryRepository;

import java.sql.*;
import java.util.UUID;

public class RepositoryDatabaseUser extends MemoryRepository<UUID, User> {

    private String URL;
    private String username;
    private String password;

    private final String SAVE_USER_SQL = "INSERT INTO users(\"firstName\",\"lastName\",\"userUUID\",\"email\")" +
            " VALUES(?, ?, ?, ?);";

    private final String GET_ALL_USERS_SQL = "SELECT * FROM users";

    private final String DELETE_USER_SQL ="DELETE FROM users\n" +
            "WHERE \"userUUID\" = ? ;";

    public RepositoryDatabaseUser(String URL, String username, String password) {
        this.URL = URL;
        this.username = username;
        this.password = password;
        getFromDatabase();

    }

    @Override
    public User delete(UUID uuid) {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(DELETE_USER_SQL)) {

            ps.setObject(1,uuid);

            ps.executeUpdate();
            return super.delete(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findOne(UUID uuid) {
        return super.findOne(uuid);
    }


    @Override
    public Iterable<User> findAll() {
        return super.findAll();
    }

    @Override
    public User update(User entity) {
        return super.update(entity);
    }

    @Override
    public User save(User entity) {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(SAVE_USER_SQL)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(4, entity.getEmail());
            ps.setObject(3, entity.getUserID());

            ps.executeUpdate();
            return super.save(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getFromDatabase(){
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_USERS_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID userUUID = (UUID) resultSet.getObject("userUUID");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                User user = new User(firstName, lastName,email);
                user.setUserID(userUUID);
                super.save(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
