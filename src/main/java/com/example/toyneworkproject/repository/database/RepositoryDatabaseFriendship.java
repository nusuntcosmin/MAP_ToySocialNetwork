package com.example.toyneworkproject.repository.database;

import com.example.toyneworkproject.domain.Friendship;
import com.example.toyneworkproject.repository.memory.MemoryRepository;
import com.example.toyneworkproject.utils.Pair;

import java.util.UUID;

public class RepositoryDatabaseFriendship extends MemoryRepository<Pair<UUID,UUID>, Friendship> {

    private String URL;
    private String username;
    private String password;

    public RepositoryDatabaseFriendship(String URL, String username, String password) {
        super();
        this.URL = URL;
        this.username = username;
        this.password = password;
    }


}
