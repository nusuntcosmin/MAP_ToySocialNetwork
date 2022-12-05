package com.example.toyneworkproject.repository.database;

import com.example.toyneworkproject.domain.request.Request;
import com.example.toyneworkproject.exceptions.RepositoryException;
import com.example.toyneworkproject.repository.Repository;
import com.example.toyneworkproject.utils.pairDataStructure.OrderPair;

import java.util.UUID;

public class RepositoryDatabaseRequest implements Repository<OrderPair<UUID,UUID>, Request> {

    private String URL;

    private String username;

    private String password;

    private final String SAVE_REQUEST_SQL ="";

    private final String DELETE_REQUEST_SQL="";




    @Override
    public Request findOne(OrderPair<UUID, UUID> uuiduuidOrderPair) throws RepositoryException {
        return null;
    }

    @Override
    public Iterable<Request> findAll() {
        return null;
    }

    @Override
    public Request save(Request entity) throws RepositoryException {
        return null;
    }

    @Override
    public Request delete(OrderPair<UUID, UUID> uuiduuidOrderPair) throws RepositoryException {
        return null;
    }

    @Override
    public Request update(Request entity) throws RepositoryException {
        return null;
    }
}
