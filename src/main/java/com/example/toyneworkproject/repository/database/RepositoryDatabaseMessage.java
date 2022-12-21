package com.example.toyneworkproject.repository.database;

import com.example.toyneworkproject.domain.Message;
import com.example.toyneworkproject.exceptions.RepositoryException;
import com.example.toyneworkproject.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositoryDatabaseMessage implements Repository<UUID,Message> {


    private final String URL;
    private final String password;
    private final String username;

    public RepositoryDatabaseMessage(String URL, String username, String password) {
        this.URL = URL;
        this.password = password;
        this.username = username;
    }


    @Override
    public Message findOne(UUID uuid) throws RepositoryException {
        return null;
    }

    public Iterable<Message> getMessagesBetweenUsers(UUID firstUserUUID, UUID secondUserUUID){
        String GET_MESSAGES_BETWEEN_USERS = "SELECT * FROM messages\n" +
                "WHERE \"fromUserUUID\" = ? AND \"toUserUUID\" = ? OR \"fromUserUUID\" = ? AND \"toUserUUID\" = ? ;";
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = connection.prepareStatement(GET_MESSAGES_BETWEEN_USERS)){
            statement.setObject(1,firstUserUUID);
            statement.setObject(2,secondUserUUID);
            statement.setObject(3,secondUserUUID);
            statement.setObject(4,firstUserUUID);
            ResultSet resultSet = statement.executeQuery();

            List<Message> messagesBetweenUsers = new ArrayList<>();
            while(resultSet.next()) {
                UUID fromUserUUID = (UUID) resultSet.getObject("fromUserUUID");
                UUID toUserUUID = (UUID) resultSet.getObject("toUserUUID");
                String messageContent = resultSet.getString("messageContent");
                Date sentDate = resultSet.getDate("sentDate");
                int sentHour = resultSet.getInt("sentHour");
                int sentMinute= resultSet.getInt("sentHour");
                int sentSecond = resultSet.getInt("sentHour");


                LocalDateTime sentTime = sentDate.toLocalDate().atTime(sentHour,sentMinute,sentSecond);
                messagesBetweenUsers.add(new Message(fromUserUUID,toUserUUID,messageContent,sentTime));

            }

            return messagesBetweenUsers;

        } catch (SQLException e) {

            int a = 3;
            return null;
        }

    }

    @Override
    public Iterable<Message> findAll() {
        return null;
    }

    @Override
    public Message save(Message entity) throws RepositoryException {
        String ADD_MESSAGE_SQL = "INSERT INTO messages(\"fromUserUUID\",\"toUserUUID\",\"messageContent\",\"sentDate\",\"sentHour\",\"sentMinute\",\"sentSecond\")\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?) ;";
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(ADD_MESSAGE_SQL)) {

            ps.setObject(1,entity.getFromUserUUID());
            ps.setObject(2,entity.getToUserUUID());
            ps.setString(3,entity.getMessageContent());
            ps.setDate(4,Date.valueOf(entity.getSentTime().toLocalDate()));
            ps.setInt(5,entity.getSentTime().getHour());
            ps.setInt(6,entity.getSentTime().getMinute());
            ps.setInt(7,entity.getSentTime().getSecond());

            ps.executeUpdate();

            return entity;
        } catch (SQLException e) {
            throw new RepositoryException("Problems ocurred when trying to save the user");
        }
    }

    @Override
    public Message delete(UUID uuid) throws RepositoryException {
        return null;
    }

    @Override
    public Message update(Message entity) throws RepositoryException {
        return null;
    }
}
