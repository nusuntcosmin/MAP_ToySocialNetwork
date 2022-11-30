package com.example.toyneworkproject.repository;


import com.example.toyneworkproject.domain.Entity;
import com.example.toyneworkproject.exceptions.RepositoryException;

public interface Repository<ID, E extends Entity<ID>> {

    E findOne(ID id) throws RepositoryException;
    Iterable<E> findAll();
    E save(E entity) throws RepositoryException;
    E delete(ID id) throws RepositoryException;
    E update(E entity) throws RepositoryException;

}

