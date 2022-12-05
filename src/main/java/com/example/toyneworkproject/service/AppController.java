package com.example.toyneworkproject.service;

import com.example.toyneworkproject.repository.database.RepositoryDatabaseFriendship;
import com.example.toyneworkproject.repository.database.RepositoryDatabaseUser;
import com.example.toyneworkproject.repository.factory.FriendshipRepositoryTypes;
import com.example.toyneworkproject.repository.factory.RepositoryFactory;
import com.example.toyneworkproject.repository.factory.UserLoginInfoRepositoryTypes;
import com.example.toyneworkproject.repository.factory.UserRepositoryTypes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AppController {

    private SecurePasswordController secureLoginController;
    private Service srv;


    public AppController(){
        secureLoginController = new SecurePasswordController();
        srv = new Service(RepositoryFactory.getInstance().getUsersRepo(UserRepositoryTypes.DATABASE_USER_REPOSITORY),
                RepositoryFactory.getInstance().getFriendshipsRepo(FriendshipRepositoryTypes.DATABASE_FRIENDSHIP_REPOSITORY));

    }

    @FXML
    private Label warnRegisterLabel;
    @FXML
    private TextField txtFld_registerUserEmail;

    @FXML
    private PasswordField passwFld_registerPassw;

    @FXML
    private PasswordField passwFld_registerReEnterPassw;

    @FXML
    private Button btnRegister;


    @FXML
    private TextField txtFld_loginUserEmail;

    @FXML
    private PasswordField passFld_loginPassw;

    @FXML
    private Button btnLogin;




    public void registerUser(){
        if(srv.emailExists(txtFld_registerUserEmail.getText())){
            warnRegisterLabel.setText("This email is already in use!");
        }
    }

    public void loginUser(){

    }
}
