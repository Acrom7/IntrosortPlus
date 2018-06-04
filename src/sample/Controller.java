package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;


public class Controller {

    @FXML
    private Button btnAddFiles;

    public void addFiles(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Files");
        fileChooser.showOpenDialog(((Node)actionEvent.getSource()).getScene().getWindow());
    }

}
