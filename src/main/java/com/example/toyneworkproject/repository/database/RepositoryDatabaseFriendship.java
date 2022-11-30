package com.example.toyneworkproject.repository.database;

import com.example.toyneworkproject.domain.Friendship;
import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.exceptions.RepositoryException;
import com.example.toyneworkproject.repository.Repository;
import com.example.toyneworkproject.repository.memory.MemoryRepository;
import com.example.toyneworkproject.utils.pairDataStructure.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

public class RepositoryDatabaseFriendship implements Repository<Pair<UUID,UUID>, Friendship> {

    private final String URL;
    private final String username;
    private final String password;

    private final String SAVE_FRIENDSHIP_SQL = "INSERT INTO " +
            "friendship(\"firstUserUUID\",\"secondUserUUID\",\"startedFrom\") " +
            " VALUES(? , ? , ?) ;";

    private final String DELETE_FRIENDSHIP_SQL ="DELETE from friendship\n" +
            "WHERE \"firstUserUUID\" = ? AND \"secondUserUUID\" = ? " +
            "OR \"firstUserUUID\" = ? AND \"secondUserUUID\" = ? ;";

    private final String FIND_ONE_FRIENDSHIP_SQL = "SELECT * from friendship" +
            " WHERE \"firstUserUUID\" = ? AND \"secondUserUUID\" = ? " +
            " OR \"firstUserUUID\" = ? AND \"secondUserUUID\" ; ";
    private final String GET_ALL_FRIENDSHIPS_SQL = "SELECT * from friendship";
    public RepositoryDatabaseFriendship(String URL, String username, String password) {
        super();
        this.URL = URL;
        this.username = username;
        this.password = password;
    }

    @Override
    public Friendship save(Friendship entity) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(SAVE_FRIENDSHIP_SQL)) {

            ps.setObject(1,entity.getFirstUserID());
            ps.setObject(2,entity.getSecondUserID());
            ps.setDate(3, Date.valueOf(entity.getStartedFrom().toLocalDate()));

            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RepositoryException("Problems ocurred when trying to save the user");
        }
    }

    @Override
    public Friendship delete(Pair<UUID, UUID> friendshipKey) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(DELETE_FRIENDSHIP_SQL)) {

            Friendship friendshipToDelete = findOne(friendshipKey);

            ps.setObject(1,friendshipKey.getFirstElement());
            ps.setObject(2,friendshipKey.getSecondElement());
            ps.setObject(3, friendshipKey.getSecondElement());
            ps.setObject(4, friendshipKey.getFirstElement());

            ps.executeUpdate();

            return friendshipToDelete;
        } catch (SQLException e) {
            throw new RepositoryException("Problems ocurred when trying to save the user");
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_FRIENDSHIPS_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            HashSet<Friendship> friendships = new HashSet<>();

            while (resultSet.next()) {
                UUID firstUserUUID = (UUID) resultSet.getObject("firstUserUUID");
                UUID secondUserUUID = (UUID) resultSet.getObject("secondUserUUID");
                LocalDate startedFrom = ((Date) resultSet.getObject("startedFrom")).toLocalDate();

                friendships.add(new Friendship(firstUserUUID,secondUserUUID,startedFrom.atStartOfDay()));
            }

            return friendships;
        } catch (SQLException e) {
            throw new RuntimeException("Probleme baza date");
        }
    }

    @Override
    public Friendship findOne(Pair<UUID, UUID> friendshipKey) {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = connection.prepareStatement(FIND_ONE_FRIENDSHIP_SQL);
             ResultSet resultSet = statement.executeQuery()) {


            UUID firstUserUUID = (UUID) resultSet.getObject("firstUserUUID");
            UUID secondUserUUID = (UUID) resultSet.getObject("secondUserUUID");
            LocalDate startedFrom = ((Date) resultSet.getObject("startedFrom")).toLocalDate();



            return new Friendship(firstUserUUID,secondUserUUID,startedFrom.atStartOfDay());
        } catch (SQLException e) {
            throw new RuntimeException("Probleme baza date");
        }
    }

    @Override
    public Friendship update(Friendship entity) {
        return null;
    }
}
