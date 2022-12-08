module com.example.toyneworkproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.toyneworkproject to javafx.fxml;
    exports com.example.toyneworkproject;
    exports com.example.toyneworkproject.guiControllers;
    exports com.example.toyneworkproject.service;
    exports com.example.toyneworkproject.domain;
    exports com.example.toyneworkproject.repository;
    exports com.example.toyneworkproject.utils.pairDataStructure;
    exports com.example.toyneworkproject.exceptions;
    exports com.example.toyneworkproject.repository.database;

    opens com.example.toyneworkproject.guiControllers to javafx.fxml;
}