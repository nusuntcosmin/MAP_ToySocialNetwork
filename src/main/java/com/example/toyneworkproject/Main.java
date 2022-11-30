package com.example.toyneworkproject;

import com.example.toyneworkproject.consoleUI.UI;
import com.example.toyneworkproject.repository.factory.FriendshipRepositoryTypes;
import com.example.toyneworkproject.repository.factory.RepositoryFactory;
import com.example.toyneworkproject.repository.factory.UserRepositoryTypes;
import com.example.toyneworkproject.repository.database.RepositoryDatabaseFriendship;
import com.example.toyneworkproject.repository.database.RepositoryDatabaseUser;
import com.example.toyneworkproject.service.Service;

public class Main {
    private static void runApp(){
        RepositoryDatabaseUser repoUser = (RepositoryDatabaseUser)
                RepositoryFactory.
                getUsersRepo(UserRepositoryTypes.DATABASE_USER_REPOSITORY);

        RepositoryDatabaseFriendship repoFriendship = (RepositoryDatabaseFriendship)
                RepositoryFactory.
                        getFriendshipsRepo(FriendshipRepositoryTypes.DATABASE_FRIENDSHIP_REPOSITORY);

        Service service = new Service(repoUser,repoFriendship);
        UI ui = new UI(service);
        ui.runApp();
    }
    public static void main(String[] args){
       Main.runApp();




    }
}
