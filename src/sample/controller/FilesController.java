package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import sample.MyFile;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FilesController implements Initializable {
  private MainController mainController;

  //Таблица файлов
  @FXML
  public TableColumn<MyFile, String> fileName;
  @FXML
  public TableColumn<MyFile, CheckBox> select;
  @FXML
  public TableView<MyFile> tvFiles;

  private final ObservableList<MyFile> myFiles = FXCollections.observableArrayList();

  public CheckBox cbAllFiles;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  // Добавляет file в список сортируемых файлов
  public void addFile(File file) {
    for (MyFile myFile : myFiles) {
      if (myFile.getFile().toString().equals(file.toString())) {
        myFile.getCheckBox().setSelected(true);
        return;
      }
    }
    myFiles.add(new MyFile(file));
    tvFiles.setItems(myFiles);
    select.setStyle("-fx-alignment: BASELINE_CENTER;");
    tvFiles.setItems(myFiles);
    fileName.setCellValueFactory(new PropertyValueFactory<>("name"));
    select.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
  }

  // Модальное окно для выбора файлов в системеу
  public void addFilesWindowsModal(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Files");
    fileChooser.setInitialDirectory(new File("resources/seq"));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SEQ files (*.seq)", "*.seq"));
    List<File> files = fileChooser.showOpenMultipleDialog(((Node) actionEvent.getSource()).getScene().getWindow());
    System.out.println(1111);
    if (files != null) {
      for (File file : files) {
        addFile(file);
      }
    }
  }

  // Удаление отмеченых файлов из таблицы
  public void deleteFiles() {
    for (int i = 0; i < myFiles.size(); ++i) {
      if (myFiles.get(i).getCheckBox().isSelected()) {
        myFiles.remove(i);
        --i;
      }
    }
  }

  public void chooseAllFiles() {
    boolean newSelect = cbAllFiles.isSelected();
    for (MyFile myFile : myFiles) {
      myFile.getCheckBox().setSelected(newSelect);
    }
  }

  // Сортировка выбранных файлов
  public void sortFiles(ActionEvent actionEvent) {
    mainController.changeTab(2);
    mainController.sortFiles();
  }

  public void init(MainController main) {
    mainController = main;
  }

  public ObservableList<MyFile> getMyFiles() {
    return myFiles;
  }
}
