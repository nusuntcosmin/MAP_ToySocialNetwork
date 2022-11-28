package com.example.toyneworkproject.repository.memory;

import com.example.toyneworkproject.domain.Entity;
import com.example.toyneworkproject.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class MemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    Map<ID,E> entities;

    @Override
    public E findOne(ID id) {
        if(id == null)
            throw new IllegalArgumentException("ID cannot be null");

        return entities.get(id);
    }

    public MemoryRepository() {
        this.entities = new HashMap<>();
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        if(entities.get(entity.getId()) != null) {
            return entity;
        }
        else entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if(id == null)
            throw new IllegalArgumentException("ID cannot be null");
        E entity= entities.get(id);
        entities.remove(id);
        return entity;
    }

    @Override
    public E update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");

        entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;
    }
}
