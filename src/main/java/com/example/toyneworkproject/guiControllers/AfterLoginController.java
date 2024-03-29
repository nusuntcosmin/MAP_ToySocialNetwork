package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.MainApp;
import com.example.toyneworkproject.domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.IOException;


public class AfterLoginController extends AbstractController {

    private User loggedUser;

    private long logInStart;

    private int numberOfRequestsReceived;
    private long logInFinish;

    @FXML
    public void friendsButtonClicked() throws IOException {
        MainApp.sendParameterToScene(loggedUser);
        MainApp.changeScene("scenes/FriendsScene.fxml");
    }
    @FXML
    public Label welcomeUserLabel;

    @FXML
    public Text welcomeUserText;

    @FXML
    public Text totalTimeSpentOnline;

    public AfterLoginController(){
            loggedUser = (User) MainApp.getParameterFromScene();
            logInStart = System.nanoTime();

    }

    @FXML
    public void logOut() throws Exception {

        logInFinish= System.nanoTime();
        service.updateOnlineTime(loggedUser,logInStart,logInFinish);
        loggedUser = null;
        MainApp.changeScene("scenes/startScene.fxml");
    }
    @FXML
    public void initialize(){
        welcomeUserText.setText("Welcome back, " + loggedUser.getFirstName() +"!");
        welcomeUserLabel.setText(loggedUser.getFirstName());

        int secondsOnline = Math.toIntExact(loggedUser.getNanoSecondsOnline() / 1000000000);
        int hoursSpentOnline = secondsOnline / 3600;
        int minutesSpentOnline = (secondsOnline % 3600) /60;
        int secondsSpentOnline = (secondsOnline) % 60;

        totalTimeSpentOnline.setText("Total time spent online :  " + hoursSpentOnline +" hours " + minutesSpentOnline + " minutes " + secondsSpentOnline + " seconds");

    }

    @FXML
    void searchButtonPressed() throws IOException {
        MainApp.sendParameterToScene(loggedUser);
        MainApp.changeScene("scenes/searchScene.fxml");

    }

    @FXML
    void messageButtonPressed() throws IOException {
        MainApp.sendParameterToScene(loggedUser);
        MainApp.changeSceneAndSize("scenes/messageScene.fxml",440,650);
    }

    @FXML
    public void requestsButtonPressed() throws IOException {
        MainApp.sendParameterToScene(loggedUser);
        MainApp.changeScene("scenes/requestsScenes.fxml");
    }
}
