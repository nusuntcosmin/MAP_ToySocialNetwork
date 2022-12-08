package com.example.toyneworkproject.repository.database;

import com.example.toyneworkproject.domain.Request;
import com.example.toyneworkproject.exceptions.RepositoryException;
import com.example.toyneworkproject.repository.Repository;
import com.example.toyneworkproject.utils.pairDataStructure.OrderPair;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

public class RepositoryDatabaseRequest implements Repository<OrderPair<UUID,UUID>, Request> {

    private String URL;

    private String username;

    private String password;

    public RepositoryDatabaseRequest(String URL, String username, String password) {
        super();
        this.URL = URL;
        this.username = username;
        this.password = password;
    }
    private final String SAVE_REQUEST_SQL ="INSERT INTO requests(\"firstUserUUID\",\"secondUserUUID\",\"sentDate\",\"requestStatus\")" +
            " VALUES(?, ? , ?, ?) ;";

    private final String DELETE_REQUEST_SQL="DELETE from requests " +
            "\nWHERE \"firstUserUUID\" =? AND \"secondUserUUID\" = ? ;";

    private final String FIND_ONE_REQUEST_SQL = "SELECT * from requests" +
            " WHERE \"firstUserUUID\" = ? AND \"secondUserUUID\" = ? ;";


    private final String UPDATE_REQUEST_STATUS_SQL ="UPDATE requests set \"requestStatus\" = ? " +
            "\nWHERE \"firstUserUUID\" = ? AND \"secondUserUUID\" = ? ;";
    private final String GET_ALL_REQUESTS_SQL = "SELECT * from requests";
    @Override
    public Request save(Request entity) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(SAVE_REQUEST_SQL)) {

            ps.setObject(1,entity.getId().getFirstElement());
            ps.setObject(2,entity.getId().getSecondElement());
            ps.setDate(3, Date.valueOf(entity.getSentTime().toLocalDate()));
            ps.setString(4,entity.getRequestStatus());
            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RepositoryException("Problems ocurred when trying to save the user");
        }
    }

    @Override
    public Request delete(OrderPair<UUID, UUID> requestKey) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(DELETE_REQUEST_SQL)) {

            Request requestToDelete = findOne(requestKey);

            ps.setObject(1,requestKey.getFirstElement());
            ps.setObject(2,requestKey.getSecondElement());


            ps.executeUpdate();

            return requestToDelete;
        } catch (SQLException e) {
            throw new RepositoryException("Problems ocurred when trying to delete the request");
        }
    }

    @Override
    public Request findOne(OrderPair<UUID, UUID> uuiduuidOrderPair) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
        PreparedStatement statement = connection.prepareStatement(FIND_ONE_REQUEST_SQL)){
         statement.setObject(1,uuiduuidOrderPair.getFirstElement());
         statement.setObject(2,uuiduuidOrderPair.getSecondElement());
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

            UUID firstUserUUID = (UUID) resultSet.getObject("firstUserUUID");
            UUID secondUserUUID = (UUID) resultSet.getObject("secondUserUUID");
            LocalDate sentTime = ((Date) resultSet.getObject("sentDate")).toLocalDate();
            String requestStatus = resultSet.getString("requestStatus");


            return new Request(firstUserUUID,secondUserUUID,sentTime.atStartOfDay(),requestStatus);
        } catch (SQLException e) {
            throw new RuntimeException("Probleme baza date");
        }
    }

    public void updateRequestStatus(OrderPair<UUID,UUID> requestID,String newRequestStatus) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement ps = connection.prepareStatement(UPDATE_REQUEST_STATUS_SQL)) {

            ps.setString(1, newRequestStatus);
            ps.setObject(2,requestID.getFirstElement());
            ps.setObject(3,requestID.getSecondElement());

            ps.executeUpdate();


        } catch (SQLException e) {
            throw new RepositoryException("Problems ocurred when trying to update the request status");
        }
    }
    @Override
    public Iterable<Request> findAll() {
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_REQUESTS_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            HashSet<Request> requests = new HashSet<>();

            while (resultSet.next()) {
                UUID firstUserUUID = (UUID) resultSet.getObject("firstUserUUID");
                UUID secondUserUUID = (UUID) resultSet.getObject("secondUserUUID");
                LocalDate sentTime = ((Date) resultSet.getObject("sentDate")).toLocalDate();
                String requestStatus = resultSet.getString("requestStatus");

                requests.add(new Request(firstUserUUID,secondUserUUID,sentTime.atStartOfDay(),requestStatus));
            }

            return requests;
        } catch (SQLException e) {
            throw new RuntimeException("Probleme baza date");
        }
    }



    @Override
    public Request update(Request entity) throws RepositoryException {
        return null;
    }
}
