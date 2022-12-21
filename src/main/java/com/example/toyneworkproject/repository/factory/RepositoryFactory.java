package com.example.toyneworkproject.repository.factory;

import com.example.toyneworkproject.domain.*;
import com.example.toyneworkproject.repository.Repository;
import com.example.toyneworkproject.repository.database.*;
import com.example.toyneworkproject.repository.memory.MemoryRepository;
import com.example.toyneworkproject.utils.pairDataStructure.OrderPair;
import com.example.toyneworkproject.utils.pairDataStructure.Pair;

import java.util.UUID;

public class RepositoryFactory {


    private final static RepositoryFactory instance = new RepositoryFactory();

    private RepositoryFactory(){
    }

    public static Repository<UUID, User> getUsersRepo(UserRepositoryTypes userRepoType){
        return switch(userRepoType){
            case DATABASE_USER_REPOSITORY -> new RepositoryDatabaseUser("jdbc:postgresql://localhost:5432/academic","postgres","parolaMea123");
            case MEMORY_USER_REPOSITORY -> new MemoryRepository<>();
        };
    }

    public static Repository<Pair<UUID,UUID>, Friendship> getFriendshipsRepo(FriendshipRepositoryTypes friendshipRepoType){
        return switch(friendshipRepoType){
            case DATABASE_FRIENDSHIP_REPOSITORY -> new RepositoryDatabaseFriendship("jdbc:postgresql://localhost:5432/academic","postgres","parolaMea123");
            case MEMORY_FRIENDSHIP_REPOSITORY -> new MemoryRepository<>();
        };
    }

    public static Repository<UUID, UserLoginInfo> getUserLoginInfoRepo(UserLoginInfoRepositoryTypes userLoginInfoRepositoryTypes){
        return switch(userLoginInfoRepositoryTypes){
            case DATABASE_USER_LOGIN_INFO_REPOSITORY -> new RepositoryDatabaseUserLoginInfo("jdbc:postgresql://localhost:5432/academic","postgres","parolaMea123");

        };
    }

    public static Repository<UUID, Message> getMessageRepo(MessageRepositoryTypes messageRepositoryTypes){
        return switch(messageRepositoryTypes){
            case DATABASE_MESSAGE_REPOSITORY -> new RepositoryDatabaseMessage("jdbc:postgresql://localhost:5432/academic","postgres","parolaMea123");

        };
    }
    public static RepositoryFactory getInstance(){
        return instance;
    }

    public static Repository<OrderPair<UUID,UUID>, Request> getRequestRepo(){
        return new RepositoryDatabaseRequest("jdbc:postgresql://localhost:5432/academic","postgres","parolaMea123");
    }
}
