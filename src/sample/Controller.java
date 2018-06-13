package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;


import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

import ru.colddegree.gen.*;
import ru.colddegree.sort.*;

public class Controller {

    //Главное окно - TabPane
    @FXML
    private TabPane tpMainWindows;

    //Группа для выбора типа файлов
    @FXML
    private ToggleGroup kindOfFiles;
    @FXML
    private RadioButton rbRandomSeq;
    @FXML
    private RadioButton rbAPSeq;
    @FXML
    private RadioButton rbKillerSeq;

    //Текстовые поля вкладки "Генерация"
    public TextField txtFirstValueRand;
    public TextField txtLastValueRand;
    public TextField txtFirstValueAP;
    public TextField txtStepAP;
    public TextField txtNumOfElem;

    //Таблица файлов
    @FXML
    private TableColumn<MyFile, String> fileName;
    @FXML
    private TableColumn<MyFile, CheckBox> select;
    @FXML
    private TableView<MyFile> tvFiles;
    private ObservableList<MyFile> myFiles = FXCollections.observableArrayList();

    //Графики
    @FXML
    private BarChart bcTime;
    @FXML
    private BarChart bcComparisons;
    @FXML
    private BarChart bcExchanges;


    private ExecutorService executor = Executors.newSingleThreadExecutor();


    //Генерация файлов для сортировка
    public void generateFiles(ActionEvent actionEvent) {
        rbAPSeq.setUserData("APSeq");
        rbKillerSeq.setUserData("KillerSeq");
        rbRandomSeq.setUserData("RandomSeq");

        //Количество элементов
        int numOfElem = Integer.parseInt(txtNumOfElem.getText());

        //Выбор типа генератора
        SequenceGenerator sequenceGenerator;
        String nameOfFile = "";
        //Определяет нужно ли ещё раз генерить
        switch (kindOfFiles.getSelectedToggle().getUserData().toString()) {
            case "APSeq":
                int initialValue = Integer.parseInt(txtFirstValueAP.getText());
                int step = Integer.parseInt(txtStepAP.getText());
                nameOfFile = "APSeq" + '_' + initialValue + '_' + step + '_' + numOfElem;
                sequenceGenerator = new SequenceGenerator(new SequentialNumberGenerator(initialValue, step), numOfElem);
                break;
            case "KillerSeq":
                nameOfFile = "KillerSeq" + '_' + numOfElem;
                sequenceGenerator = new SequenceGenerator(new MedianOf3KillerNumberGenerator(numOfElem), numOfElem);
                break;
            case "RandomSeq":
                int from = Integer.parseInt(txtFirstValueRand.getText());
                int to = Integer.parseInt(txtLastValueRand.getText());
                nameOfFile = "RandomSeq" + '_' + from + '_' + to + '_' + numOfElem;
                sequenceGenerator = new SequenceGenerator(new RandomNumberGenerator(from, to), numOfElem);
                break;
            default:
                sequenceGenerator = new SequenceGenerator(new MedianOf3KillerNumberGenerator(numOfElem), numOfElem);
        }

        //Генерация
        String seq = ".seq";
        nameOfFile = "resources/seq/" + nameOfFile;
        File file = new File(nameOfFile + seq);

        //Подбор имени файла если оригинал (или предыдущие) уже заняты
        for (int i = 1; i <= 10000; i++) {
            if (file.exists()) {
                file = new File(nameOfFile + "_" + i + seq);
            } else {
                break;
            }
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Генерация последовательности
        sequenceGenerator.generateFile(file.toString());

        //Добавление в таблицу
        addFile(file);
    }

    /**
     * Добавляет file в массив сортируемых файлов
     * и обновляет данные в таблице
     *
     * @param file
     */
    private void addFile(File file) {
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
        fileName.setCellValueFactory(new PropertyValueFactory<MyFile, String>("name"));
        select.setCellValueFactory(new PropertyValueFactory<MyFile, CheckBox>("checkBox"));
    }

    //Добавление файлов для сортировки
    public void addFiles(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Files");
        fileChooser.setInitialDirectory(new File("resources/seq"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SEQ files (*.seq)", "*.seq"));
        List<File> files = fileChooser.showOpenMultipleDialog(((Node) actionEvent.getSource()).getScene().getWindow());

        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                addFile(files.get(i));
            }
        }
    }

    public void sortFiles(ActionEvent actionEvent) {
        bcTime.getData().clear();
        bcExchanges.getData().clear();
        bcComparisons.getData().clear();

        //Переключаемся на вкладку
        tpMainWindows.getSelectionModel().select(2);

        XYChart.Series timeQuickSort = new XYChart.Series();
        timeQuickSort.setName("QuickSort");
        XYChart.Series timeIntroSort = new XYChart.Series();
        timeIntroSort.setName("IntroSort");

        XYChart.Series cmpQuickSort = new XYChart.Series();
        cmpQuickSort.setName("QuickSort");
        XYChart.Series cmpIntroSort = new XYChart.Series();
        cmpIntroSort.setName("IntroSort");

        XYChart.Series excQuickSort = new XYChart.Series();
        excQuickSort.setName("QuickSort");
        XYChart.Series excIntroSort = new XYChart.Series();
        excIntroSort.setName("IntroSort");


        executor.shutdown();
        executor = Executors.newSingleThreadExecutor();

        for (int i = 0; i < myFiles.size(); i++) {
            if (myFiles.get(i).getCheckBox().isSelected()) {
                Sorter introSorter = new IntroSorter();
                Sorter quickSorter = new QuickSorter();



                final int idx = i;


                Task introsortTask = new Task<Long>() {
                    @Override
                    protected Long call() throws Exception {
                        int[] seq = getSequenceFromFile(myFiles.get(idx).getFile());

                        long startTime = System.currentTimeMillis();
                        introSorter.sort(seq);
                        long endTime = System.currentTimeMillis();

                        return endTime - startTime;
                    }
                };

                introsortTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        //Добавляем данные в серию
                        try {
                            timeIntroSort.getData().add(new XYChart.Data(myFiles.get(idx).getName(), introsortTask.get()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        cmpIntroSort.getData().add(new XYChart.Data(myFiles.get(idx).getName(), introSorter.getComparisons()));
                        excIntroSort.getData().add(new XYChart.Data(myFiles.get(idx).getName(), introSorter.getExchanges()));
                    }
                });

                executor.execute( new Thread(introsortTask) );




                Task quicksortTask = new Task<Long>() {
                    @Override
                    protected Long call() throws Exception {
                        int[] seq = getSequenceFromFile(myFiles.get(idx).getFile());

                        long startTime = System.currentTimeMillis();
                        quickSorter.sort(seq);
                        long endTime = System.currentTimeMillis();

                        return endTime - startTime;
                    }
                };

                quicksortTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        try {
                            timeQuickSort.getData().add(new XYChart.Data(myFiles.get(idx).getName(), quicksortTask.get()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        cmpQuickSort.getData().add(new XYChart.Data(myFiles.get(idx).getName(), quickSorter.getComparisons()));
                        excQuickSort.getData().add(new XYChart.Data(myFiles.get(idx).getName(), quickSorter.getExchanges()));
                    }
                });

                executor.execute( new Thread(quicksortTask) );

            }
        }

        //Добавляем на график
//        bcTime.getData().add(timeIntroSort);
//        bcTime.getData().add(timeQuickSort);
        bcTime.getData().addAll(timeIntroSort, timeQuickSort);
        bcComparisons.getData().addAll(cmpIntroSort, cmpQuickSort);
        bcExchanges.getData().addAll(excIntroSort, excQuickSort);
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

    public void stopExecution(ActionEvent actionEvent) {
        executor.shutdown();
    }
}
