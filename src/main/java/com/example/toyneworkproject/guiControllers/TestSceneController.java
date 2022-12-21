package com.example.toyneworkproject.guiControllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class TestSceneController {

    public TestSceneController(){

    }

    @FXML
    private VBox ceva;

    @FXML
    private VBox chatBox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    void initialize(){

        scrollPane.setContent(chatBox);
        Label l = new Label();
        Label l1 = new Label();
        chatBox.setFillWidth(true);
        l.setText("ceva das d sa dsa dsa d sa dsad sa dsa dsa d sad sa dsa d sa das d asdas");
        l1.setText("cevadsad ad sa dsa d sa d as a sabdshabdbsadh bah dbsa bhsabdhbsa dasvhd vahsdv savd asds");
        l.setAlignment(Pos.CENTER_LEFT);
        l1.setAlignment(Pos.CENTER_RIGHT);


        l.setWrapText(true);
        l1.setWrapText(true);

        l.setMaxWidth(Double.MAX_VALUE);
        l1.setMaxWidth(Double.MAX_VALUE);

        chatBox.getChildren().add(l);
        chatBox.getChildren().add(new Label());
        chatBox.getChildren().add(l1);
    }


}
