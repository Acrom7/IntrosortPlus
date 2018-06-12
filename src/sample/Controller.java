package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;


public class Controller {

    public Button btnSort;
    //Группа для выбора типа файлов
    @FXML private ToggleGroup kindOfFiles;
    @FXML private RadioButton rbRandomSeq;
    @FXML private RadioButton rbAPSeq;
    @FXML private RadioButton rbKillerSeq;

    //Текстовые поля вкладки "Генерация"
    @FXML private TextField txtFirstValue;
    @FXML private TextField txtStepSeq;
    @FXML private TextField txtNumOfFiles;
    @FXML private TextField txtNumOfElem;

    //Таблица файлов
    @FXML private TableColumn<MyFile, String> fileName;
    @FXML private TableColumn<MyFile, CheckBox> select;
    @FXML private TableView<MyFile> tvFiles;
    private ObservableList<MyFile> myFiles = FXCollections.observableArrayList();

    //Графики
    @FXML private BarChart bcTime;
    @FXML private BarChart bcComparisons;
    @FXML private BarChart bcExchanges;

    // Включаем/Отключаем доступность полей txtFirstValue && txtStepSeq
    public void chooseAPSeq(ActionEvent actionEvent) {
        txtFirstValue.setDisable(false);
        txtStepSeq.setDisable(false);
    }
    public void chooseRandomSeq(ActionEvent actionEvent) {
        txtFirstValue.setDisable(true);
        txtStepSeq.setDisable(true);
    }
    public void chooseKillerSeq(ActionEvent actionEvent) {
        txtFirstValue.setDisable(true);
        txtStepSeq.setDisable(true);
    }

    //Генерация файлов для сортировка
    public void btnGenerateFiles(ActionEvent actionEvent) {
        rbAPSeq.setUserData("APSeq");
        rbKillerSeq.setUserData("KillerSeq");
        rbRandomSeq.setUserData("RandomSeq");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText("Ваш выбор");

        String s;
        s = "Тип: " + kindOfFiles.getSelectedToggle().getUserData().toString() + '\n' ;
        s += "Количество: " + txtNumOfFiles.getText();
        alert.setContentText(s);
        alert.show();
    }

    //Добавление файлов для сортировки
    public void addFiles(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Files");
        fileChooser.setInitialDirectory(new File("resources/seq"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SEQ files (*.seq)","*.seq"));
        List<File> files = fileChooser.showOpenMultipleDialog(((Node)actionEvent.getSource()).getScene().getWindow());

        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                myFiles.add(new MyFile(files.get(i)));
            }
            select.setStyle("-fx-alignment: BASELINE_CENTER;");
            tvFiles.setItems(myFiles);
            fileName.setCellValueFactory(new PropertyValueFactory<MyFile, String>("name"));
            select.setCellValueFactory(new PropertyValueFactory<MyFile, CheckBox>("checkBox"));
        }
    }

   @FXML
    public void testDrawChart() {
        final String austria = "Austria";
        final String brazil = "Brazil";
        final String france = "France";
        final String italy = "Italy";
        final String usa = "USA";
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("2004");
        series2.getData().add(new XYChart.Data(austria, 57401.85));
        series2.getData().add(new XYChart.Data(brazil, 41941.19));
        series2.getData().add(new XYChart.Data(france, 45263.37));
        series2.getData().add(new XYChart.Data(italy, 117320.16));
        series2.getData().add(new XYChart.Data(usa, 14845.27));
        bcTime.getData().addAll(series2);
    }

    public void sortFiles(ActionEvent actionEvent) {

    }
}
