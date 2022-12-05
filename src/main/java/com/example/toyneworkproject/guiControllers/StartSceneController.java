package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.MainApp;
import com.example.toyneworkproject.domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.io.IOException;

public class StartSceneController extends AbstractController {
    @FXML
    public Label warnLoginLabel;

    public StartSceneController(){
        int a = 2 + 3;
    }
    @FXML
    public Text warnTxtLogin;
    @FXML
    public TextField txtEmailLogin;
    @FXML
    public PasswordField passLogin;


    @FXML
    public void initialize(){

    }
    @FXML
    public void logInButtonClicked(){
        try{
            warnLoginLabel.setMinWidth(Region.USE_PREF_SIZE);
            if(txtEmailLogin.getText().isEmpty() || passLogin.getText().isEmpty())
                throw new RuntimeException("Please fulfill all the fields");

            User loggingUser = service.findByEmail(txtEmailLogin.getText());
            securePasswordController.loginUser(loggingUser.getUserID(),passLogin.getText());

            MainApp.sendParameterToScene(loggingUser);
            MainApp.changeScene("scenes/afterLoginScene.fxml");

        }
        catch(Exception Ex){
            warnTxtLogin.setText(Ex.getMessage());
        }

    }
    @FXML
    public void registerButtonClicked(){
        try {
            MainApp.changeScene("scenes/registerScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}