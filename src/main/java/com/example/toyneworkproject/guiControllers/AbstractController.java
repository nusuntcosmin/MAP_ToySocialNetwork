package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.repository.database.RepositoryDatabaseRequest;
import com.example.toyneworkproject.repository.factory.FriendshipRepositoryTypes;
import com.example.toyneworkproject.repository.factory.RepositoryFactory;
import com.example.toyneworkproject.repository.factory.UserRepositoryTypes;
import com.example.toyneworkproject.service.SecurePasswordController;
import com.example.toyneworkproject.service.Service;

public abstract class AbstractController {

    protected static Service service= new Service(RepositoryFactory.getUsersRepo(UserRepositoryTypes.DATABASE_USER_REPOSITORY),
                RepositoryFactory.getFriendshipsRepo(FriendshipRepositoryTypes.DATABASE_FRIENDSHIP_REPOSITORY), (RepositoryDatabaseRequest) RepositoryFactory.getRequestRepo());

    protected static SecurePasswordController securePasswordController = new SecurePasswordController();

}
