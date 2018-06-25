package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;


import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import ru.colddegree.gen.*;
import ru.colddegree.sort.*;

public class Controller implements Initializable {

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

    public Text txtSuccessAdd;

    //Текстовые поля вкладки "Генерация"
    public TextField txtFirstValueRand;
    public TextField txtLastValueRand;
    public TextField txtFirstValueAP;
    public TextField txtStepAP;
    public TextField txtNumOfElem;

    public Button btnGenerateFiles;

    //Таблица файлов
    @FXML
    private TableColumn<MyFile, String> fileName;
    @FXML
    private TableColumn<MyFile, CheckBox> select;
    @FXML
    private TableView<MyFile> tvFiles;
    private ObservableList<MyFile> myFiles = FXCollections.observableArrayList();
    public CheckBox cbAllFiles;

    //Графики
    @FXML
    private BarChart<String, Double> bcTime;
    @FXML
    private BarChart<String, Long> bcComparisons;
    @FXML
    private BarChart<String, Long> bcExchanges;
    public Button btnStopSort;
    public Text txtProgressBar;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnStopSort.setTooltip(new Tooltip("Прекращает выполнение сортировки\n"));
        btnStopSort.getTooltip().setShowDelay(new Duration(100));

        //Вводить можно только цифры
        setNumbersOnly(txtNumOfElem);
        setNumbersOnly(txtFirstValueAP);
        setNumbersOnly(txtStepAP);
        setNumbersOnly(txtFirstValueRand);
        setNumbersOnly(txtLastValueRand);
    }

    //Генерация файлов для сортировка
    public void generateFiles(ActionEvent actionEvent) {
        rbAPSeq.setUserData("APSeq");
        rbKillerSeq.setUserData("KillerSeq");
        rbRandomSeq.setUserData("RandomSeq");

        //Количество элементов
        int numOfElem = Integer.parseInt(getTextFromField(txtNumOfElem));
        if (numOfElem <= 0) {
            throwAlertWindow("Количество генерируемых элементов\nдолжно быть положительным");
            return;
        }

        //Выбор типа генератора
        SequenceGenerator sequenceGenerator;
        String nameOfFile = "";
        //Определяет нужно ли ещё раз генерить
        switch (kindOfFiles.getSelectedToggle().getUserData().toString()) {
            case "APSeq":
                int initialValue = Integer.parseInt(getTextFromField(txtFirstValueAP));
                int step = Integer.parseInt(getTextFromField(txtStepAP));
                // filename: "[Arithmetic] 0 step 1, 100 elems.seq"
                nameOfFile = "[Arithmetic] " + initialValue + " step " + step + ", " + numOfElem + " elems";
                sequenceGenerator = new SequenceGenerator(new SequentialNumberGenerator(initialValue, step), numOfElem);
                break;
            case "KillerSeq":
                // filename: "[Killer] 100 elems.seq"
                nameOfFile = "[Killer] " + numOfElem + " elems";
                sequenceGenerator = new SequenceGenerator(new MedianOf3KillerNumberGenerator(numOfElem), numOfElem);
                break;
            case "RandomSeq":
                int from = Integer.parseInt(getTextFromField(txtFirstValueRand));
                int to = Integer.parseInt(getTextFromField(txtLastValueRand));
                if (from > to) {
                    throwAlertWindow("Значения \"От\" и \"До\" не верны");
                    return;
                }
                // filename: "[Random] 0 to 10, 100 elems.seq"
                nameOfFile = "[Random] " + from + " to " + to + ", " + numOfElem + " elems";
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
                file = new File(nameOfFile + "(" + i + ")" + seq);
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



        //Убираем надпись по времени
        txtSuccessAdd.setVisible(true);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                txtSuccessAdd.setVisible(false);
            }
        };

        timer.schedule(timerTask, 1000);
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

        if (myFiles.isEmpty()) {
            throwAlertWindow("Выберите файлы для сортировки");
            return;
        }

        bcTime.getData().clear();
        bcExchanges.getData().clear();
        bcComparisons.getData().clear();

        //Переключаемся на вкладку
        tpMainWindows.getSelectionModel().select(2);

        XYChart.Series<String, Double> timeQuickSort = new XYChart.Series<>();
        timeQuickSort.setName("QuickSort");
        XYChart.Series<String, Double> timeIntroSort = new XYChart.Series<>();
        timeIntroSort.setName("IntroSort");

        XYChart.Series<String, Long> cmpQuickSort = new XYChart.Series<>();
        cmpQuickSort.setName("QuickSort");
        XYChart.Series<String, Long> cmpIntroSort = new XYChart.Series<>();
        cmpIntroSort.setName("IntroSort");

        XYChart.Series<String, Long> excQuickSort = new XYChart.Series<>();
        excQuickSort.setName("QuickSort");
        XYChart.Series<String, Long> excIntroSort = new XYChart.Series<>();
        excIntroSort.setName("IntroSort");


        executor = Executors.newSingleThreadExecutor();

        for (MyFile file : myFiles) {
            if (!file.getCheckBox().isSelected())
                continue;

            Sorter introSorter = new IntroSorter();

            // добавляем задачу сортировки интроспективной сортировкой
            Task<Double> introsortTask = createSorterTask(file, introSorter);

            introsortTask.setOnSucceeded( (event) -> {
                //Добавляем данные в серию
                try {
                    timeIntroSort.getData().add(new XYChart.Data<>(file.getName(), (Double) introsortTask.get()));
                    cmpIntroSort.getData().add(new XYChart.Data<>(file.getName(), introSorter.getComparisons()));
                    excIntroSort.getData().add(new XYChart.Data<>(file.getName(), introSorter.getExchanges()));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });

            executor.execute(new Thread(introsortTask));



            Sorter quickSorter = new QuickSorter();

            // добавляем задачу сортировки быстрой сортировкой
            Task<Double> quicksortTask = createSorterTask(file, quickSorter);

            quicksortTask.setOnSucceeded( (event) -> {
                //Добавляем данные в серию
                try {
                    timeQuickSort.getData().add(new XYChart.Data<>(file.getName(), (Double) quicksortTask.get()));
                    cmpQuickSort.getData().add(new XYChart.Data<>(file.getName(), quickSorter.getComparisons()));
                    excQuickSort.getData().add(new XYChart.Data<>(file.getName(), quickSorter.getExchanges()));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });

            executor.execute(new Thread(quicksortTask));
        }

        //Добавляем на график
        bcTime.getData().addAll(timeIntroSort, timeQuickSort);
        bcComparisons.getData().addAll(cmpIntroSort, cmpQuickSort);
        bcExchanges.getData().addAll(excIntroSort, excQuickSort);

        executor.shutdown();
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

    public void stopExecution(ActionEvent actionEvent) throws IOException{
        txtProgressBar.setVisible(false);
        executor.shutdownNow();
    }

    //Используя регулярку можно вводить только Integer
    public void setNumbersOnly(TextField textField) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\0?[0-]?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        textField.setTextFormatter(new TextFormatter<>(integerFilter));
//        textField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
    }

    //Удаление отмеченых файлов из таблицы
    public void deleteFiles(ActionEvent actionEvent) {
        for (int i = 0; i < myFiles.size(); ++i) {
            if (myFiles.get(i).getCheckBox().isSelected()) {
                myFiles.remove(i);
                --i;
            }
        }
    }

    public void chooseAllFiles(ActionEvent actionEvent) {
        boolean newSelect = cbAllFiles.isSelected();
        for (int i = 0; i < myFiles.size(); i++) {
            myFiles.get(i).getCheckBox().setSelected(newSelect);
        }
    }

    public void throwAlertWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Внимание!");
        alert.setHeaderText("ОШИБКА");
        alert.setContentText(message);
        alert.show();
    }

    //Возвращает текст из textField если он есть, иначе PromptText
    public String getTextFromField(TextField textField) {
        if (textField.getText().isEmpty()) {
            return textField.getPromptText();
        } else {
            return textField.getText();
        }
    }

    private Task<Double> createSorterTask(MyFile file, Sorter sorter) {
        final int ITERATIONS = 15;

        return new Task<Double>() {
            @Override
            protected Double call() throws Exception {
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
}
