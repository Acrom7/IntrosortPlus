package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/index.fxml"));
        primaryStage.setTitle("GUI for Introsort-plus");
        primaryStage.setScene(new Scene(root, 636, 400));
        primaryStage.setMinHeight(root.minHeight(-1));
        primaryStage.setMinWidth(root.minWidth(-1));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
