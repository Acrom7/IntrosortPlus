package sample.controller;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Common {
  // Модальное окно с текстом ошибки
  public static void throwAlertWindow(Stage stage, String message) {
    JFXDialogLayout layout = new JFXDialogLayout();
    layout.setBody(new Label(message));
    layout.setHeading(new Text("Внимание!"));
    JFXAlert<Void> alert = new JFXAlert<>(stage);

    JFXButton close = new JFXButton("Закрыть");
    close.setButtonType(JFXButton.ButtonType.RAISED);
    close.getStyleClass().add("button-raised");
    layout.setActions(close);
    close.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        alert.close();
      }
    });

    alert.setOverlayClose(true);
    alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
    alert.setContent(layout);
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.show();
  }

}
