package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class RequestsSceneController extends AbstractController{

    @FXML
    public TableView<User> receivedRequests;
    @FXML
    public TableView<User> sentRequests;
    @FXML
    public TableColumn<User, String> sentRequestNameColumn;
    @FXML
    public TableColumn<User, String> sentRequestEmailColumn;
    @FXML
    public TableColumn<User, String> receivedRequestNameColumn;
    @FXML
    public TableColumn<User, String> receivedRequestEmailColumn;


    public RequestsSceneController(){

    }

    @FXML
    public void initialize(){
        reloadReceivedRequests();
        reloadSentRequests();
    }

    public void reloadSentRequests(){

    }
    public void reloadReceivedRequests(){

    }
}
