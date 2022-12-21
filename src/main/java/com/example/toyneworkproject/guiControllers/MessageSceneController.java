package com.example.toyneworkproject.guiControllers;

import com.example.toyneworkproject.MainApp;
import com.example.toyneworkproject.domain.Message;
import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.exceptions.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ListIterator;

public class MessageSceneController extends AbstractController{

    public MessageSceneController(){
        loggedUser = (User) MainApp.getParameterFromScene();

    }

    private void displayReceivedMessage(String messageContent){
        Label labelMessage = new Label(messageContent);
        labelMessage.setFont(new Font("Arial",18));
        labelMessage.setWrapText(true);
        labelMessage.setMaxWidth(Double.MAX_VALUE);
        labelMessage.setAlignment(Pos.CENTER_LEFT);
        labelMessage.setStyle("-fx-border-radius: 5 5 5 5;" +
                ";-fx-border-color: white;" +
                "-fx-background-color: rgb(169,169,169)");
        vBoxMessage.getChildren().add(labelMessage);
    }

    private void displaySentMessage(String messageContent){
        Label labelMessage = new Label(messageContent);
        labelMessage.setFont(new Font("Arial",18));
        labelMessage.setWrapText(true);
        labelMessage.setMaxWidth(Double.MAX_VALUE);
        labelMessage.setAlignment(Pos.CENTER_RIGHT);
        labelMessage.setStyle("-fx-border-radius: 5 5 5 5;" +
                ";-fx-border-color: white;" +
                "-fx-background-color: rgb(0,255,255)");
        vBoxMessage.getChildren().add(labelMessage);
    }
    private void reloadMessagesWithUser(User user){
        ArrayList<Message> orderedMessagesWithUser = service.getOrderedByTimeMessagesBetweenUsers(loggedUser.getUserID(),user.getUserID());
        ListIterator<Message> messageListIterator = orderedMessagesWithUser.listIterator();

        while(messageListIterator.hasNext()){
            Message previousMessage = null;

            if(messageListIterator.hasPrevious()){
                previousMessage = messageListIterator.previous();
                messageListIterator.next();
            }
            Message currentMessage = messageListIterator.next();

            if(previousMessage!= null && !currentMessage.getFromUserUUID().equals(previousMessage.getFromUserUUID()))
                vBoxMessage.getChildren().add(new Label());

            if (currentMessage.getFromUserUUID().equals(loggedUser.getUserID()))
                displaySentMessage(currentMessage.getMessageContent());

            if(currentMessage.getFromUserUUID().equals(user.getUserID()))
                displayReceivedMessage(currentMessage.getMessageContent());
            }
        }




    @FXML
    void sentButtonPressed() throws RepositoryException {
        if(!messageToSendTxtField.getText().isEmpty() && !messageToSendTxtField.getText().isBlank() && !friendsList.getSelectionModel().getSelectedItems().isEmpty()){
            service.addMessage(loggedUser.getUserID(),friendsList.getSelectionModel().getSelectedItem().getUserID(),messageToSendTxtField.getText());
            displaySentMessage(messageToSendTxtField.getText());
        }
    }
    @FXML
    public void initialize() throws RepositoryException {

        scrollPaneMessage.setContent(vBoxMessage);
        vBoxMessage.getStyleClass().add("chatbox");
        vBoxMessage.setFillWidth(true);
        vBoxMessage.heightProperty().addListener(observable -> scrollPaneMessage.setVvalue(1D));


        usersObservableList.setAll(service.getUserFriendsForUser(loggedUser.getUserID()));
        reloadFriendsListView();
        FilteredList<User> filteredData =   new FilteredList<>(usersObservableList, b->true);

        friendsSearchBar.textProperty().addListener((observable,oldValue,newValue) -> {
                filteredData.setPredicate(user -> {
                        if(newValue.isEmpty() || newValue.isBlank())
                            return true;

                        String searchKeyword = newValue.toLowerCase();
                        if((user.getFirstName() + " " + user.getLastName()).toLowerCase().contains(searchKeyword))
                            return true;

                        return user.getEmail().toLowerCase().contains(searchKeyword);
                    });
                });

        friendsList.setItems(filteredData);


    }

    private void reloadFriendsListView(){
        friendsList.setItems(usersObservableList);
    }

    @FXML
    private ListView<User> friendsList;

    private final User loggedUser;
    @FXML
    private TextField friendsSearchBar;

    @FXML
    private TextField messageToSendTxtField;

    @FXML
    private Text messageUserReceiverText;

    @FXML
    private ScrollPane scrollPaneMessage;

    @FXML
    private Button sendButton;

    @FXML
    private VBox vBoxMessage;

    ObservableList<User> usersObservableList = FXCollections.observableArrayList();


    @FXML
    void returnMousePressed() throws IOException {
        MainApp.sendParameterToScene(loggedUser);
        MainApp.changeSceneAndSize("scenes/afterLoginScene.fxml",400,680);
    }

    @FXML
    void usersSearchItemSelectionChanged() {
        if(!friendsList.getSelectionModel().isEmpty()){
            vBoxMessage.getChildren().clear();
            messageUserReceiverText.setText(friendsList.getSelectionModel().getSelectedItem().getFirstName() + " "+friendsList.getSelectionModel().getSelectedItem().getLastName());
            reloadMessagesWithUser(friendsList.getSelectionModel().getSelectedItem());
        }


    }

}
