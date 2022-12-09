package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.MainApp;
import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.domain.UserRequestWrapper;
import com.example.toyneworkproject.exceptions.RepositoryException;
import com.example.toyneworkproject.exceptions.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.stream.Collectors;

public class RequestsSceneController extends AbstractController{


    private final long timeSpentOnThisSceneStart;
    private long timeSpentOnThisSceneFinal;
    @FXML
    private Button acceptButton;

    @FXML
    private Button hideButton;

    @FXML
    private Button rejectButton;

    @FXML
    private Button unsendButton;

    @FXML
    void acceptButtonPressed() throws ServiceException, RepositoryException {
        service.addFriendship(loggedUser.getUserID(),receivedRequestsTableView.getSelectionModel().getSelectedItem().getRequest().getId().getFirstElement());
        service.deleteRequest(receivedRequestsTableView.getSelectionModel().getSelectedItem().getRequest().getId());
        reloadReceivedRequests();
        selectedSentRequestText.setText("");

    }

    @FXML
    void hideButtonPressed() throws RepositoryException {
        service.updateRequestStatus(receivedRequestsTableView.getSelectionModel().getSelectedItem().getRequest().getId(), "Hidden");
        reloadReceivedRequests();
        selectedReceivedRequestText.setText("");
    }

    @FXML
    void unsendButtonPressed() throws RepositoryException {
        service.deleteRequest(sentRequestsTableView.getSelectionModel().getSelectedItem().getRequest().getId());
        reloadSentRequests();
        selectedSentRequestText.setText("User deleted succesfully");
    }

    @FXML
    void rejectButtonPressed() throws RepositoryException {
        service.deleteRequest(receivedRequestsTableView.getSelectionModel().getSelectedItem().getRequest().getId());
        reloadReceivedRequests();
        selectedReceivedRequestText.setText("");
    }


    @FXML
    private TableColumn<UserRequestWrapper, String> sentRequestSentTime;

    @FXML
    private TableColumn<UserRequestWrapper, String> receivedRequestSentTime;
    private final User loggedUser;
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
    void returnButtonPressed() throws Exception {
        timeSpentOnThisSceneFinal = System.nanoTime();
        service.updateOnlineTime(loggedUser,timeSpentOnThisSceneStart,timeSpentOnThisSceneFinal);
        MainApp.sendParameterToScene(loggedUser);
        MainApp.changeScene("scenes/afterLoginScene.fxml");
    }

    public RequestsSceneController(){
        timeSpentOnThisSceneStart = System.nanoTime();
        loggedUser = (User) MainApp.getParameterFromScene();

    }

    public void initializeReceivedRequestTable(){
        receivedRequestNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        receivedRequestEmailColumn.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        receivedRequestSentTime.setCellValueFactory(new PropertyValueFactory<>("requestSentTime"));

    }



    public void initializeSentRequestTable(){
        sentRequestNameColumn.setCellValueFactory(new PropertyValueFactory<UserRequestWrapper,String>("userName"));
        sentRequestEmailColumn.setCellValueFactory(new PropertyValueFactory<UserRequestWrapper,String>("userEmail"));
        sentRequestSentTime.setCellValueFactory(new PropertyValueFactory<UserRequestWrapper,String>("requestSentTime"));
    }
    @FXML
    public void receivedRequestsItemSelectionChanged(){
            if(!receivedRequestsTableView.getSelectionModel().getSelectedItems().isEmpty()){
                enableReceivenRequestButtons();
                selectedReceivedRequestText.setText("Selected user : " + receivedRequestsTableView.getSelectionModel().getSelectedItem().getUserName());
            }
    }
    @FXML
    public void sentRequestsItemSelectionChanged(){
            if(!sentRequestsTableView.getSelectionModel().getSelectedItems().isEmpty()){
                unsendButton.setVisible(true);
                unsendButton.setDisable(false);
                selectedSentRequestText.setText("Selected user : " + sentRequestsTableView.getSelectionModel().getSelectedItem().getUserName());
            }

    }

    private void enableReceivenRequestButtons(){
        acceptButton.setVisible(true);
        rejectButton.setVisible(true);
        hideButton.setVisible(true);

        acceptButton.setDisable(false);
        rejectButton.setDisable(false);
        hideButton.setDisable(false);
    }
    private void disableReceivenRequestButtons(){
        acceptButton.setVisible(false);
        rejectButton.setVisible(false);
        hideButton.setVisible(false);

        acceptButton.setDisable(true);
        rejectButton.setDisable(true);
        hideButton.setDisable(true);
    }
    @FXML
    public void initialize(){
        initializeReceivedRequestTable();
        initializeSentRequestTable();
        reloadReceivedRequests();
        reloadSentRequests();

        disableReceivenRequestButtons();
        unsendButton.setVisible(false);
        unsendButton.setDisable(true);
    }

    public void reloadSentRequests(){
        ObservableList<UserRequestWrapper> sentRequestUsers = FXCollections.
               observableArrayList(
                       service.usersThatReceiveRequestsFrom(loggedUser.getUserID())
        );


        sentRequestsTableView.setItems(sentRequestUsers);

    }
    public void reloadReceivedRequests(){
        ObservableList<UserRequestWrapper> receivedRequestUsers = FXCollections.
                observableArrayList(

                        service.usersThatSentRequestTo(loggedUser.getUserID())
                                .stream()
                                .filter(c->!c.getRequest().getRequestStatus().equals("Hidden"))
                                .collect(Collectors.toList())


                );

        enableReceivenRequestButtons();
        receivedRequestsTableView.setItems(receivedRequestUsers);
    }
}
