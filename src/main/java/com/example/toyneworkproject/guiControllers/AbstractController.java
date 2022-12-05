package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.repository.factory.FriendshipRepositoryTypes;
import com.example.toyneworkproject.repository.factory.RepositoryFactory;
import com.example.toyneworkproject.repository.factory.UserRepositoryTypes;
import com.example.toyneworkproject.service.SecurePasswordController;
import com.example.toyneworkproject.service.Service;

public abstract class AbstractController {

    protected Service service= new Service(RepositoryFactory.getUsersRepo(UserRepositoryTypes.DATABASE_USER_REPOSITORY),
                RepositoryFactory.getFriendshipsRepo(FriendshipRepositoryTypes.DATABASE_FRIENDSHIP_REPOSITORY));

    protected SecurePasswordController securePasswordController = new SecurePasswordController();

}
