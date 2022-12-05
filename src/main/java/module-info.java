module com.example.toyneworkproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.toyneworkproject to javafx.fxml;
    exports com.example.toyneworkproject;
    exports com.example.toyneworkproject.guiControllers;
    opens com.example.toyneworkproject.guiControllers to javafx.fxml;
}