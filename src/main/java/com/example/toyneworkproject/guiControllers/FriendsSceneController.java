package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.MainApp;
import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.domain.UserFriendshipWrapper;
import com.example.toyneworkproject.exceptions.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;

public class FriendsSceneController extends AbstractController{
    @FXML
    private Label warnLabel;
    private final User loggedUser;

    @FXML
    private TextField searchFriendTextField;





    public FriendsSceneController(){
        loggedUser = (User) MainApp.getParameterFromScene();
        timeSpentOnThisSceneStart = System.nanoTime();

    }

    @FXML
    public void initialize() throws RepositoryException {
        deleteFriendButton.setVisible(false);
        deleteFriendButton.setDisable(true);
        initializefriendsTableView();
        reloadFriendsTableView();

        FilteredList<UserFriendshipWrapper> filteredUserFriendshipList = new FilteredList<>(usersFriends, b->true);
        searchFriendTextField.textProperty().addListener((observable,newValue,oldValue) ->{
            filteredUserFriendshipList.setPredicate(UserFriendshipWrapper ->{

                deleteFriendButton.setDisable(true);
                deleteFriendButton.setVisible(false);

                if(newValue.isEmpty() || newValue.isBlank())
                    return true;

                String searchKeyword =  newValue.toLowerCase();

                if((UserFriendshipWrapper.getFirstName()).toLowerCase().contains(searchKeyword))
                    return true;

                if(UserFriendshipWrapper.getLastName().toLowerCase().contains(searchKeyword))
                    return true;

                return false;

            });
        });

        SortedList<UserFriendshipWrapper> sortedData = new SortedList<>(filteredUserFriendshipList);
        sortedData.comparatorProperty().bind(friendsTableView.comparatorProperty());

        friendsTableView.setItems(sortedData);
    }

    ObservableList<UserFriendshipWrapper> usersFriends = FXCollections.
            observableArrayList();

    public void reloadFriendsTableView() throws RepositoryException {
        usersFriends.setAll(service.getFriendsForUser(loggedUser.getUserID()));
        friendsTableView.setItems(usersFriends);

    }
    public void initializefriendsTableView(){
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        startedDate.setCellValueFactory(new PropertyValueFactory<>("startedDate"));

    }
    @FXML
    private Button deleteFriendButton;

    @FXML
    private TableColumn<UserFriendshipWrapper, String> emailColumn;

    @FXML
    private TableColumn<UserFriendshipWrapper, String> firstNameColumn;

    @FXML
    private TableView<UserFriendshipWrapper> friendsTableView;

    @FXML
    private TableColumn<UserFriendshipWrapper, String> lastNameColumn;

    @FXML
    private TableColumn<UserFriendshipWrapper, LocalDateTime> startedDate;


    private final long timeSpentOnThisSceneStart;

    @FXML
    void deleteFriendButtonClicked() throws RepositoryException {
        service.deleteFriendship(friendsTableView.getSelectionModel().getSelectedItem().getFriendship().getFirstUserID(),
                friendsTableView.getSelectionModel().getSelectedItem().getFriendship().getSecondUserID());

        reloadFriendsTableView();
        deleteFriendButton.setVisible(false);
        deleteFriendButton.setDisable(true);
        warnLabel.setText("Friendship succesfully removed");
    }

    @FXML
    void friendsTableViewItemSelectionChanged() {

            if(!friendsTableView.getSelectionModel().getSelectedItems().isEmpty()){
                deleteFriendButton.setDisable(false);
                deleteFriendButton.setVisible(true);
                warnLabel.setText("");
            }

    }

    @FXML
    void returnButtonPressed() throws Exception {
        long timeSpentOnThisSceneFinal = System.nanoTime();
        service.updateOnlineTime(loggedUser,timeSpentOnThisSceneStart, timeSpentOnThisSceneFinal);
        MainApp.sendParameterToScene(loggedUser);
        MainApp.changeScene("scenes/afterLoginScene.fxml");
    }
}

