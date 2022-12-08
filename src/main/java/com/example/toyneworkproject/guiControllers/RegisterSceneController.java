package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.MainApp;
import com.example.toyneworkproject.domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class RegisterSceneController extends AbstractController {

    public RegisterSceneController(){

    }

    @FXML
    public Text warnTextRegister;
    @FXML
    public TextField txtEmailRegister;
    @FXML
    public TextField txtFirstNameRegister;
    @FXML
    public TextField txtLastNameRegister;
    @FXML
    public PasswordField passEnterRegister;
    @FXML
    public PasswordField passReEnterRegister;

    @FXML
    public Button bt;
    private void checkNullFields() throws Exception{
        if(txtEmailRegister.getText().isEmpty() ||
                txtLastNameRegister.getText().isEmpty() ||
                    txtFirstNameRegister.getText().isEmpty() ||
                        passEnterRegister.getText().isEmpty() ||
                            passReEnterRegister.getText().isEmpty())
            throw new Exception("Please fill al the fields !");
    }

    private void checkPasswords() throws Exception{
        if(!passReEnterRegister.getText().equals(passEnterRegister.getText()))
            throw new Exception("Passwords do not match!");
    }

    private void checkEmailAvailibility() throws Exception{
        if(service.emailExists(txtEmailRegister.getText()))
            throw new Exception("Email is already in use");
    }

    @FXML
    public void returnButtonPressed() throws IOException {
        MainApp.changeScene("scenes/startScene.fxml");
    }
    public void registerButtonClicked(){
        try{
            checkNullFields();
            checkEmailAvailibility();
            checkPasswords();
            User newUser = service.addUser(txtFirstNameRegister.getText(),
                                            txtLastNameRegister.getText(),
                                                txtEmailRegister.getText());

            securePasswordController.registerUser(newUser.getUserID(), passEnterRegister.getText());
            warnTextRegister.setText("User created succesfully, go back to the login page");
            txtEmailRegister.clear();
            txtFirstNameRegister.clear();
            txtLastNameRegister.clear();
            passEnterRegister.clear();
            passReEnterRegister.clear();

        }catch (Exception Ex){
            warnTextRegister.setText(Ex.getMessage());
        }

    }
}
