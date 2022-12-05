package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.MainApp;
import com.example.toyneworkproject.domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;



public class AfterLoginController extends AbstractController {

    private User loggedUser;

    private long logInStart;

    private int numberOfRequestsReceived;

    private long logInFinish;

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
        service.updateOnlineTime(loggedUser,loggedUser.getNanoSecondsOnline() + logInFinish - logInStart);
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

        totalTimeSpentOnline.setText("Total time spent online : Hours " + hoursSpentOnline +" Minutes " + minutesSpentOnline + " Seconds " + secondsSpentOnline);

    }

}
