package sample.controller;

import com.jfoenix.controls.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sample.MyFile;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
  public AnchorPane generate;
  public AnchorPane files;
  public AnchorPane charts;
  public JFXButton closeButton;
  public JFXButton btnMin;

  //Главное окно - TabPane
  @FXML
  private TabPane tpMainWindows;

  // Контроллер вкладок
  @FXML
  public GenerateController generateController;
  @FXML
  public FilesController filesController;
  @FXML
  public ChartsController chartsController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    generateController.init(this);
    filesController.init(this);
    chartsController.init(this);
  }

  public void addGenerateFile(File file) {
    filesController.addFile(file);
  }

  public int sortFiles() {
    return chartsController.sortFiles();
  }

  public ObservableList<MyFile> getMyFiles() {
    return filesController.getMyFiles();
  }

  public void changeTab(int tabNumber) {
    tpMainWindows.getSelectionModel().select(tabNumber);
  }

  public void closeButtonAction(ActionEvent actionEvent) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  public void handleMinAction(ActionEvent actionEvent) {
    Stage stage = (Stage) btnMin.getScene().getWindow();
    stage.setIconified(true);
  }

  public Stage getStage() {
    return (Stage) tpMainWindows.getScene().getWindow();
  }

  public void maxWindow(MouseEvent mouseEvent) {
    Stage stage = (Stage) btnMin.getScene().getWindow();
    stage.setFullScreen(true);
    stage.setFullScreenExitHint(" ");
  }
}
