package sample.controller;

import javafx.scene.control.Alert;

public class Common {

  public static void throwAlertWindow(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Внимание!");
    alert.setHeaderText("ОШИБКА");
    alert.setContentText(message);
    alert.show();
  }

}
