package sample;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;

public class Controller {
    private MainWindow mainwindow;
    private ArrayList<Button> massButtonElem;
    private int countElem = 0;
    private static final int MAX_ELEM_IN_ROW = 10;

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
    private Button rezultButton;
    @FXML
    private Button stepButton;
    @FXML
    private Button addElemButton;
    @FXML
    private GridPane elemBox;
    @FXML
    private TextArea informationArea;
    @FXML
    void initialize() {
    }

    @FXML
    private void buttonRezultPressed() {
        if(countElem==0){
            new Alert(AlertType.INFORMATION, "Данные не введены!").showAndWait();
            return;
        }
        Integer[] data = new Integer[massButtonElem.size()];
        for(Button a : massButtonElem ){
            data[massButtonElem.indexOf(a)] = Integer.valueOf(a.getText());
        }
        int[] sortData = mainwindow.sort(data);
        elemBox.getChildren().clear();
        countElem = sortData.length;
        massButtonElem = new ArrayList<Button>();
        elemBox.getChildren().remove(0, elemBox.getChildren().size());
        for (int i = 0; i < sortData.length; i++) {
            massButtonElem.add(new EditableButton(Integer.toString(sortData[i])));
            elemBox.add(massButtonElem.get(i), i % MAX_ELEM_IN_ROW, i / MAX_ELEM_IN_ROW);
            GridPane.setHalignment(massButtonElem.get(i), HPos.CENTER);
            massButtonElem.get(i).setDisable(true);
        }
        informationArea.setText("Данные\nотсортированы!");
        rezultButton.setDisable(true);
    }
    @FXML
    private void buttonInsertPressed() {
        stringToButtons(insertDataLine.getText());
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
    private void buttonClearPressed(){
        massButtonElem = null;
        countElem = 0;
        elemBox.getChildren().clear();
        elemBox.add(addElemButton, 0, 0);
        rezultButton.setDisable(false);
        informationArea.clear();
        insertDataLine.clear();
    }

    @FXML
    private void buttonExitPressed() {
        System.exit(0);
    }

    private void addElemToBox(Button newElem){
        GridPane.setHalignment(newElem, HPos.CENTER);
        elemBox.getChildren().remove(addElemButton);
        elemBox.add(newElem, countElem % MAX_ELEM_IN_ROW,countElem / MAX_ELEM_IN_ROW);
        elemBox.add(addElemButton, (countElem+1) % MAX_ELEM_IN_ROW, (countElem+1) / MAX_ELEM_IN_ROW);
    }
    @FXML
    private void buttonAddElemPressed() {
        if(massButtonElem == null){
            massButtonElem = new ArrayList<Button>();
        }
        massButtonElem.add(new EditableButton("0"));
        addElemToBox(massButtonElem.get(countElem));
        countElem++;
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
    class EditableButton extends Button {
        TextField tf = new TextField();

        public EditableButton(String text) {
            setText(text);
            tf.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent keyEvent) {
                            if (!"-0123456789".contains(keyEvent.getCharacter())) {
                                keyEvent.consume();
                            }
                        }
                    }
            );
            setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    tf.setText(getText());
                    setGraphic(tf);
                    setText("");
                    tf.requestFocus();
                    tf.selectAll();
                }
            });
            tf.textProperty().addListener((obs) -> tf.setPrefWidth(0.5 * tf.getFont().getSize() * tf.getText().length() + 30));
            tf.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) {
                    if (isStringCorrect(tf.getText())) {
                        setText(tf.getText());
                        setGraphic(null);
                    } else {
                        new Alert(AlertType.ERROR, "Вы ввели некорректные данные.").showAndWait();
                        tf.requestFocus();
                        tf.selectAll();
                    }
                }
            });

            /*tf.setOnAction(ae -> {
                if (isStringCorrect(tf.getText())) {// this is where you would validate the text
                    setText(tf.getText());
                    setGraphic(null);
                } else {
                    setText("1");
                }
            });*/
        }
    }

    private boolean isStringCorrect(String str) {
        boolean result = true;
        try {
            int test = Integer.parseInt(str.trim().replaceAll("(\\s)+", " "));
        } catch (NumberFormatException nfe) {
            result = false;
        }
        return result;
    }

    private int[] stringToIntArray(String str) throws NumberFormatException {
        int[] array = Arrays.stream(str.trim().replaceAll("(\\s)+", " ").split(" ")).mapToInt(Integer::parseInt).toArray();
        return array;
    }

    private void stringToButtons(String str) {
        try {
            int[] numbers = stringToIntArray(str);
            countElem = numbers.length;
            massButtonElem = new ArrayList<Button>();
            elemBox.getChildren().remove(0, elemBox.getChildren().size());
            for (int i = 0; i < numbers.length; i++) {
                massButtonElem.add(new EditableButton(Integer.toString(numbers[i])));
                elemBox.add(massButtonElem.get(i), i % MAX_ELEM_IN_ROW, i / MAX_ELEM_IN_ROW);
                GridPane.setHalignment(massButtonElem.get(i), HPos.CENTER);
            }
            elemBox.add(addElemButton, countElem % MAX_ELEM_IN_ROW, countElem / MAX_ELEM_IN_ROW);
        } catch (NumberFormatException nfe) {
            new Alert(AlertType.ERROR, "Вы ввели некорректные данные.").showAndWait();
        }
    }
}

