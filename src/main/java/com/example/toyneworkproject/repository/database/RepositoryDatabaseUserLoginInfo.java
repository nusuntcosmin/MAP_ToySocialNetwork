package com.example.toyneworkproject.repository.database;


import com.example.toyneworkproject.domain.UserLoginInfo;
import com.example.toyneworkproject.exceptions.RepositoryException;
import com.example.toyneworkproject.repository.Repository;


import java.sql.*;

import java.util.UUID;

public class RepositoryDatabaseUserLoginInfo implements Repository<UUID, UserLoginInfo> {
    private String URL;
    private String username;
    private String password;

    private final String SAVE_LOGIN_INFO_SQL = "INSERT INTO \"usersLoginInfo\"(\"userUUID\",\"salt\",\"encryptedPassword\")" +
            " VALUES(?, ?, ?);";


    private final String GET_USER_LOGIN_INFO_SQL = "SELECT * FROM \"usersLoginInfo\" WHERE \"userUUID\" = ? ; ";

    private final String DELETE_USER_LOGIN_INFO_SQL ="DELETE FROM \"usersLoginInfo\" \n" +
            "WHERE \"userUUID\" = ? ;";

    private final String UPDATE_USER_LOGIN_INFO_SQL ="UPDATE users set \"encryptedPassword\" = ? , \"salt\" = ?" +
            "\nWHERE \"userUUID\" = ? ;";

    public RepositoryDatabaseUserLoginInfo(String URL, String username, String password) {
        this.URL = URL;
        this.username = username;
        this.password = password;

    }

    @Override
    public UserLoginInfo delete(UUID uuid) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(DELETE_USER_LOGIN_INFO_SQL)) {
                ps.setObject(1,uuid);
                ps.executeUpdate();
                return null;

        } catch (SQLException e) {
            throw new RepositoryException("An user with that ID could not be found");
        }
    }

    @Override
    public UserLoginInfo findOne(UUID uuid) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = connection.prepareStatement(GET_USER_LOGIN_INFO_SQL)){
                 statement.setObject(1,uuid);
                 ResultSet resultSet = statement.executeQuery();
                 resultSet.next();
                UUID userUUID = (UUID) resultSet.getObject("userUUID");
                String userSalt = resultSet.getString("salt");
                String userEncryptedPassword = resultSet.getString("encryptedPassword");

                UserLoginInfo userLoginInfoFound = new UserLoginInfo(userEncryptedPassword, userSalt,userUUID);

            return userLoginInfoFound;


        } catch (SQLException e) {
            throw new RepositoryException("User Info cannot be found");
        }
    }

    @Override
    public Iterable<UserLoginInfo> findAll() {
        return null;
    }



    @Override
    public UserLoginInfo update(UserLoginInfo entity) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(UPDATE_USER_LOGIN_INFO_SQL)) {

            ps.setString(2, entity.getUserSalt());
            ps.setString(3, entity.getUserEncryptedPassword());
            ps.setObject(1, entity.getUserUUID());

            ps.executeUpdate();

            return null;

        } catch (SQLException e) {
            throw new RepositoryException("Problems ocurred when trying to update the user");
        }

    }

    @Override
    public UserLoginInfo save(UserLoginInfo entity) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(SAVE_LOGIN_INFO_SQL)) {

            ps.setObject(1, entity.getUserUUID());
            ps.setString(2, entity.getUserSalt());
            ps.setString(3, entity.getUserEncryptedPassword());

            ps.executeUpdate();

            return null;
        } catch (SQLException e) {
            throw new RepositoryException("");
        }

    }
}
