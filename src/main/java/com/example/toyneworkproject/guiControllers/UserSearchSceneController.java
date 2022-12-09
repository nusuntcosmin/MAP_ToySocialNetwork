package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.MainApp;
import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.exceptions.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;


import java.time.LocalDateTime;

public class UserSearchSceneController extends AbstractController{

    long timeSpentOnThisSceneStart;
    long timeSpentOnThisSceneFinal;
    public UserSearchSceneController(){
        loggedUser = (User) MainApp.getParameterFromScene();
        timeSpentOnThisSceneStart = System.nanoTime();
    }

    @FXML
    private TextField searchTextField;

    @FXML
    private Label warnLabel;

    @FXML
    public void initialize(){
        sentRequestButton.setVisible(false);
        sentRequestButton.setDisable(true);
        userSearchObservableList.setAll(service.getUsersWithout(loggedUser.getUserID()));
        initializeUsersTable();
        reloadUsersTableView();

        FilteredList<User> filteredData =   new FilteredList<>(userSearchObservableList, b->true);

        searchTextField.textProperty().addListener((observable,oldValue,newValue) ->{
            filteredData.setPredicate(user -> {
                warnLabel.setText("");
                sentRequestButton.setVisible(false);
                sentRequestButton.setDisable(true);
                selectedUser.setText("");
                if(newValue.isEmpty() ||   newValue.isBlank())
                    return true;


                String searchKeyword = newValue.toLowerCase();

                if((user.getFirstName() + " " + user.getLastName()).toLowerCase().contains(searchKeyword))
                    return true;

                return user.getEmail().toLowerCase().contains(searchKeyword);

            });
        });

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(usersTableView.comparatorProperty());

        usersTableView.setItems(sortedData);

    }

    private void initializeUsersTable(){
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User,String>("email"));

    }

    private void reloadUsersTableView(){
        usersTableView.setItems(userSearchObservableList);
    }

    private final User loggedUser;
    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private Text selectedUser;

    @FXML
    private Button sentRequestButton;

    @FXML
    private TableView<User> usersTableView;

    ObservableList<User> userSearchObservableList = FXCollections.observableArrayList();

    @FXML
    void requestButtonPressed() throws RepositoryException {
        try{
            service.saveRequest(loggedUser.getUserID(),usersTableView.getSelectionModel().getSelectedItem().getUserID(), "Sent", LocalDateTime.now());
            warnLabel.setText("Friendship request sent succesfully");
        }
        catch(Exception Ex)
        {
            warnLabel.setText(Ex.getMessage());
        }


    }

    @FXML
    void inputMethodTextChanged() {
        warnLabel.setText("");
        sentRequestButton.setDisable(true);
        sentRequestButton.setVisible(false);
        selectedUser.setText("");
    }
    @FXML
    void returnButtonPressed() throws Exception {
        timeSpentOnThisSceneFinal = System.nanoTime();
        service.updateOnlineTime(loggedUser,timeSpentOnThisSceneStart,timeSpentOnThisSceneFinal);
        MainApp.sendParameterToScene(loggedUser);
        MainApp.changeScene("scenes/afterLoginScene.fxml");
    }



    @FXML
    void usersTableViewSelectionChanged() {
        if(!usersTableView.getSelectionModel().getSelectedItems().isEmpty()){
            sentRequestButton.setVisible(true);
            sentRequestButton.setDisable(false);
            warnLabel.setText("");

            selectedUser.setText("Selected user:" + usersTableView.getSelectionModel().getSelectedItem().toString());
        }
    }

}
