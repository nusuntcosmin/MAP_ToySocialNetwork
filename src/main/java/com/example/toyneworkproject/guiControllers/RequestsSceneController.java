package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.MainApp;
import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.domain.UserRequestWrapper;
import com.example.toyneworkproject.domain.request.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class RequestsSceneController extends AbstractController{

    @FXML
    private TableColumn<UserRequestWrapper, String> sentRequestSentTime;

    @FXML
    private TableColumn<UserRequestWrapper, String> receivedRequestSentTime;
    private User loggedUser;
    @FXML
    private Text selectedReceivedRequestText;

    @FXML
    private Text selectedSentRequestText;

    @FXML
    private TableColumn<UserRequestWrapper,String> receivedRequestEmailColumn;

    @FXML
    private TableColumn<UserRequestWrapper, String> receivedRequestNameColumn;

    @FXML
    private TableView<UserRequestWrapper> receivedRequestsTableView;

    @FXML
    private TableColumn<UserRequestWrapper, String> sentRequestEmailColumn;

    @FXML
    private TableColumn<UserRequestWrapper, String> sentRequestNameColumn;

    @FXML
    private TableView<UserRequestWrapper> sentRequestsTableView;

    @FXML
    void goBack() throws IOException {
        MainApp.sendParameterToScene(loggedUser);
        MainApp.changeScene("scenes/afterLoginScene.fxml");
    }


    public RequestsSceneController(){
        loggedUser = (User) MainApp.getParameterFromScene();

    }



    public void initializeReceivedRequestTable(){
        receivedRequestNameColumn.setCellValueFactory(new PropertyValueFactory<UserRequestWrapper,String>("userName"));
        receivedRequestEmailColumn.setCellValueFactory(new PropertyValueFactory<UserRequestWrapper,String>("userEmail"));
        receivedRequestSentTime.setCellValueFactory(new PropertyValueFactory<UserRequestWrapper,String>("requestSentTime"));
    }

    public void initializeSentRequestTable(){
        sentRequestNameColumn.setCellValueFactory(new PropertyValueFactory<UserRequestWrapper,String>("userName"));
        sentRequestEmailColumn.setCellValueFactory(new PropertyValueFactory<UserRequestWrapper,String>("userEmail"));
        sentRequestSentTime.setCellValueFactory(new PropertyValueFactory<UserRequestWrapper,String>("requestSentTime"));
    }
    @FXML
    public void initialize(){
        initializeReceivedRequestTable();
        initializeSentRequestTable();
        reloadReceivedRequests();
        reloadSentRequests();
    }

    public void reloadSentRequests(){
        ObservableList<UserRequestWrapper> receivedRequestUsers = FXCollections.
               observableArrayList(
                 service.usersThatSentRequestTo(loggedUser.getUserID())
        );

        sentRequestsTableView.setItems(receivedRequestUsers);

    }
    public void reloadReceivedRequests(){
        ObservableList<UserRequestWrapper> receivedRequestUsers = FXCollections.
                observableArrayList(
                        service.usersThatSentRequestTo(loggedUser.getUserID())
                );

        receivedRequestsTableView.setItems(receivedRequestUsers);
    }
}
