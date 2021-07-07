package sample;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Controller {
    private MainWindow mainwindow;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label menuLabel;

    @FXML
    private Button helpButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button loadButton;

    @FXML
    private Button authorsButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField insertDataLine;

    @FXML
    private Button insertButton;

    @FXML
    private Button runButton;

    @FXML
    private Button stepButton;

    @FXML
    private Button addElemButton;

    @FXML
    private HBox elemBox;

    @FXML
    void initialize() {

    }

    @FXML
    private void buttonRunPressed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Сообщение");
        alert.setHeaderText(null);
        alert.setContentText("Нажата клавиша запуска!");
        alert.showAndWait();
    }
    @FXML
    private void buttonInsertPressed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Сообщение");
        alert.setHeaderText(null);
        alert.setContentText("Вы ввели: " + insertDataLine.getText());
        alert.showAndWait();
    }
    @FXML
    private void buttonStepPressed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Сообщение");
        alert.setHeaderText(null);
        alert.setContentText("Нажата клавиша шага алгоритма!");
        alert.showAndWait();
    }
    @FXML
    private void buttonSavePressed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Сообщение");
        alert.setHeaderText(null);
        alert.setContentText("Нажата клавиша сохранения!");
        alert.showAndWait();
    }
    @FXML
    private void buttonLoadPressed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Сообщение");
        alert.setHeaderText(null);
        alert.setContentText("Нажата клавиша загрузки!");
        alert.showAndWait();
    }
    @FXML
    private void buttonAuthorsPressed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Сообщение");
        alert.setHeaderText(null);
        alert.setContentText("Нажата клавиша авторы!");
        alert.showAndWait();
    }
    @FXML
    private void buttonExitPressed() {
        System.exit(0);
    }
    @FXML
    private void buttonAddElemPressed() {
        elemBox.getChildren().addAll(new Button("new elem"));
    }
    @FXML
    private void buttonHelpPressed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Сообщение");
        alert.setHeaderText(null);
        alert.setContentText("Нажата клавиша справки!");
        alert.showAndWait();
    }
    public void setMainWindow(MainWindow mw){
        mainwindow = mw;
    }
}

