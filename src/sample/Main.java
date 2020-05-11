package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
  private double xOffset = 0;
  private double yOffset = 0;

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("view/index.fxml"));
    primaryStage.setTitle("GUI for Introsort-plus");
    primaryStage.setScene(new Scene(root, 636, 400));
    primaryStage.setMinHeight(root.minHeight(-1));
    primaryStage.setMinWidth(root.minWidth(-1));
    primaryStage.initStyle(StageStyle.TRANSPARENT);

    // Возможность передвигать окно
    Scene scene = new Scene(new formDecorator(primaryStage, root));
    scene.setFill(null);

    // Добавляем стили
    scene.getStylesheets().add(Main.class.getResource("resources/jfoenix-components.css").toExternalForm());
    scene.getStylesheets().add(Main.class.getResource("resources/index.css").toExternalForm());

    primaryStage.setScene(scene);

    primaryStage.show();
  }

  public class formDecorator extends TabPane {
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage primaryStage;

    public formDecorator(Stage stage, Node node) {
      super();

      primaryStage = stage;
      this.setPadding(new javafx.geometry.Insets(0, 0, 0, 0));

      this.getChildren().add(node);

      this.setOnMousePressed((MouseEvent event) -> {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
      });
      this.setOnMouseDragged((MouseEvent event) -> {
        primaryStage.setX(event.getScreenX() - xOffset);
        primaryStage.setY(event.getScreenY() - yOffset);
      });

    }

  }

  public static void main(String[] args) {
    launch(args);
  }
}
