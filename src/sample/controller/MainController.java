package sample.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import sample.MyFile;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
  public AnchorPane generate;
  public AnchorPane files;
  public AnchorPane charts;

  //Главное окно - TabPane
  @FXML
  private TabPane tpMainWindows;

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

  public void sortFiles() {
    chartsController.sortFiles();
  }

  public ObservableList<MyFile> getMyFiles() {
    return filesController.getMyFiles();
  }

  public void changeTab(int tabNumber) {
    tpMainWindows.getSelectionModel().select(tabNumber);
  }
}
