package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import ru.colddegree.gen.MedianOf3KillerNumberGenerator;
import ru.colddegree.gen.RandomNumberGenerator;
import ru.colddegree.gen.SequenceGenerator;
import ru.colddegree.gen.SequentialNumberGenerator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.UnaryOperator;

public class GenerateController implements Initializable {
  private MainController mainController;

  public TextField txtFirstValueRand;
  public TextField txtLastValueRand;
  public TextField txtFirstValueAP;
  public TextField txtStepAP;
  public TextField txtNumOfElem;

  public Text txtSuccessAdd;
  public Button btnGenerateFiles;
  //Группа для выбора типа файлов
  @FXML
  private ToggleGroup kindOfFiles;
  @FXML
  private RadioButton rbRandomSeq;
  @FXML
  private RadioButton rbAPSeq;
  @FXML
  private RadioButton rbKillerSeq;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setNumbersOnly(txtNumOfElem);
    setNumbersOnly(txtFirstValueAP);
    setNumbersOnly(txtStepAP);
    setNumbersOnly(txtFirstValueRand);
    setNumbersOnly(txtLastValueRand);
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

  //Генерация файлов для сортировка
  public void generateFiles() {
    rbAPSeq.setUserData("APSeq");
    rbKillerSeq.setUserData("KillerSeq");
    rbRandomSeq.setUserData("RandomSeq");

    //Количество элементов
    int numOfElem = Integer.parseInt(getTextFromField(txtNumOfElem));
    if (numOfElem <= 0) {
      Common.throwAlertWindow("Количество генерируемых элементов\nдолжно быть положительным");
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
          Common.throwAlertWindow("Значение \"От\" должно быть небольше \"До\"");
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
      boolean create = file.createNewFile();
      if (!create) {
        throw new Error("Can't create new file.");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    //Генерация последовательности
    sequenceGenerator.generateFile(file.toString());

    //Добавление в таблицу
    mainController.addGenerateFile(file);

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

  //Возвращает текст из textField если он есть, иначе PromptText
  public String getTextFromField(TextField textField) {
    if (textField.getText().isEmpty()) {
      return textField.getPromptText();
    } else {
      return textField.getText();
    }
  }

  public void init(MainController main) {
    mainController = main;
  }
}
