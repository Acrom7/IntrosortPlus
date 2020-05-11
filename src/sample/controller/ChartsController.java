package sample.controller;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ru.colddegree.sort.HeapSorter;
import ru.colddegree.sort.IntroSorter;
import ru.colddegree.sort.Sorter;
import sample.MyFile;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChartsController implements Initializable {
  public JFXHamburger show;
  public AnchorPane anchorPane;
  private MainController mainController;
  //Графики
  @FXML
  public BarChart<String, Double> bcTime;
  @FXML
  public BarChart<String, Long> bcComparisons;
  @FXML
  public BarChart<String, Long> bcExchanges;

  public Text txtProgressBar;

  private ExecutorService executor = Executors.newSingleThreadExecutor();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    show.setPadding(new Insets(10, 5, 10, 5));
    JFXRippler rippler = new JFXRippler(show, JFXRippler.RipplerMask.CIRCLE, JFXRippler.RipplerPos.FRONT);

    JFXListView<Label> list = new JFXListView<>();
    Label label = new Label("Сортировка");
    label.setStyle("-fx-min-width: 160px;");
    label.setOnMouseClicked(e -> this.sortFiles());
    list.getItems().add(label);

    Label label2 = new Label("Остановить");
    label2.setStyle("-fx-min-width: 160px;");
    label2.setOnMouseClicked(e -> this.stopExecution());
    list.getItems().add(label2);

    JFXPopup popup = new JFXPopup(list);
    rippler.setOnMouseClicked(e -> popup.show(rippler, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));
    AnchorPane.setRightAnchor(rippler, 5.0);
    AnchorPane.setTopAnchor(rippler, -2.0);
    anchorPane.getChildren().add(rippler);
  }

  public void sortFiles() {
    ObservableList<MyFile> myFiles = mainController.getMyFiles();

    if (myFiles.isEmpty()) {
      Common.throwAlertWindow("Выберите файлы для сортировки");
      return;
    }

    clearChartsData();

    XYChart.Series<String, Double> timeHeapSort = new XYChart.Series<>();
    timeHeapSort.setName("HeapSort");
    XYChart.Series<String, Double> timeIntroSort = new XYChart.Series<>();
    timeIntroSort.setName("IntroSort");

    XYChart.Series<String, Long> cmpHeapSort = new XYChart.Series<>();
    cmpHeapSort.setName("HeapSort");
    XYChart.Series<String, Long> cmpIntroSort = new XYChart.Series<>();
    cmpIntroSort.setName("IntroSort");

    XYChart.Series<String, Long> excHeapSort = new XYChart.Series<>();
    excHeapSort.setName("HeapSort");
    XYChart.Series<String, Long> excIntroSort = new XYChart.Series<>();
    excIntroSort.setName("IntroSort");

    executor = Executors.newSingleThreadExecutor();

    for (MyFile file : myFiles) {
      if (!file.getCheckBox().isSelected())
        continue;

      Sorter introSorter = new IntroSorter();

      // добавляем задачу сортировки интроспективной сортировкой
      sortAndShow(file, introSorter, timeIntroSort, cmpIntroSort, excIntroSort);

      Sorter heapSorter = new HeapSorter();

      // добавляем задачу сортировки пирамидальной сортировкой
      sortAndShow(file, heapSorter, timeHeapSort, cmpHeapSort, excHeapSort);
    }

    //Добавляем на график
    bcTime.getData().add(timeIntroSort);
    bcTime.getData().add(timeHeapSort);
    bcComparisons.getData().add(cmpIntroSort);
    bcComparisons.getData().add(cmpHeapSort);
    bcExchanges.getData().add(excIntroSort);
    bcExchanges.getData().add(excHeapSort);

    executor.shutdown();
  }

  private void clearChartsData() {
    bcTime.getData().clear();
    bcExchanges.getData().clear();
    bcComparisons.getData().clear();
  }

  private void sortAndShow(MyFile file, Sorter introSorter, XYChart.Series<String, Double> timeSort, XYChart.Series<String, Long> cmpSort, XYChart.Series<String, Long> excSort) {
    Task<Double> introsortTask = createSorterTask(file, introSorter);

    introsortTask.setOnSucceeded((event) -> {
      //Добавляем данные в серию
      try {
        timeSort.getData().add(new XYChart.Data<>(file.getName(), introsortTask.get()));
        cmpSort.getData().add(new XYChart.Data<>(file.getName(), introSorter.getComparisons()));
        excSort.getData().add(new XYChart.Data<>(file.getName(), introSorter.getExchanges()));
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    });

    executor.execute(new Thread(introsortTask));
  }

  public void stopExecution() {
    txtProgressBar.setVisible(false);
    executor.shutdownNow();
  }

  private Task<Double> createSorterTask(MyFile file, Sorter sorter) {
    final int ITERATIONS = 15;

    return new Task<>() {
      @Override
      protected Double call() {
        txtProgressBar.setVisible(true);

        List<Long> execTimes = new ArrayList<>();

        for (int i = 0; i < ITERATIONS; i++) {
          int[] seq = getSequenceFromFile(file.getFile());
          sorter.sort(seq);
          execTimes.add(sorter.getExecutionTime());
        }

        execTimes.sort(null);
        execTimes.remove(0);
        execTimes.remove(execTimes.size() - 1);

        txtProgressBar.setVisible(false);
        return execTimes.stream().mapToLong(val -> val).average().orElse(0.0);
      }
    };
  }

  /**
   * Считывает последовательность чисел из файла filepath в массив и возвращает его
   *
   * @param filepath путь к файлу
   * @return массив чисел из файла
   */
  private static int[] getSequenceFromFile(File filepath) {
    int[] seq = null;

    try (Scanner scanner = new Scanner(filepath)) {

      seq = new int[scanner.nextInt()];

      for (int i = 0; i < seq.length; i++) {
        seq[i] = scanner.nextInt();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return seq;
  }

  public void init(MainController main) {
    mainController = main;
  }
}
