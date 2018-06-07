package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;


public class Controller {

    @FXML public RadioButton rbDecType;
    @FXML public RadioButton rbIncType;
    @FXML public RadioButton rbConstType;
    @FXML public ToggleGroup kindOfFiles;
    @FXML public TextField txtConstValue;
    @FXML public TextField txtNumOfFiles;


    public void addFiles(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Files");
        fileChooser.showOpenDialog(((Node)actionEvent.getSource()).getScene().getWindow());
    }

    // Включаем/Отключаем доступность поля constValue
    public void chooseConstType(ActionEvent actionEvent) {
        rbConstType.setUserData("Константа");
        txtConstValue.setDisable(false);
    }

    public void chooseDecType(ActionEvent actionEvent) {
        rbDecType.setUserData("Убывающая");
        txtConstValue.setDisable(true);
    }

    public void chooseIncType(ActionEvent actionEvent) {
        rbIncType.setUserData("Возрастающая");
        txtConstValue.setDisable(true);
    }

    //Генерация файлов для сортировка
    public void btnGenerateFiles(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText("Ваш выбор");

        String s;
        s = "Тип: " + kindOfFiles.getSelectedToggle().getUserData().toString() + '\n' ;
        s += "Количество: " + txtNumOfFiles.getText();
        alert.setContentText(s);
        alert.show();
    }
}
