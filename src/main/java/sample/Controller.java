package sample;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class Controller {
    private MainWindow mainwindow;
    private ArrayList<EditableButton> massButtonElem;
    private int countElem = 0;
    private static final int MAX_ELEM_IN_ROW = 10;
    private int swapState = 0;
    private EditableButton first, second;
    stepState currState = null;

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
    private AnchorPane drawField;
    @FXML
    private Button runButton;
    @FXML
    void initialize() {
    }

    @FXML
    private void buttonRunPressed(){
        if(countElem==0){
            new Alert(AlertType.INFORMATION, "Данные не введены!").showAndWait();
            return;
        }
        Integer[] data = new Integer[massButtonElem.size()];
        for(Button a : massButtonElem ){
            data[massButtonElem.indexOf(a)] = Integer.valueOf(a.getText());
        }
        elemBox.getChildren().removeAll(addElemButton);
        for (int i = 0; i < countElem; i++) {
            elemBox.getChildren().get(i).setDisable(true);
        }
        mainwindow.startStepSort(data, drawField);
        stepButton.setDisable(false);
        loadButton.setDisable(true);
        insertButton.setDisable(true);
        runButton.setDisable(true);
        currState = null;
    }
    @FXML
    private void buttonStepPressed() {
        if(currState==null){
            currState = new stepState();
            currState.state = WeakHeap.State.initial;
            currState.first = 0;currState.second = 0;
            currState.length = 0;
        }
        clearStyleButtons();
        System.out.println("first: "+ currState.first);
        System.out.println("second: "+ currState.second);
        System.out.println("length: "+ currState.length);
        System.out.println("state: "+currState.state);
        switch (currState.state) {
            case preBuilding, building-> {
                if(currState.isChanged){
                    stepSwapButtons(currState.first, currState.second, "", "-fx-background-color: blue");
                    informationArea.setText("Этап 1. Построение кучи\nменяем элементы "+ " "+ massButtonElem.get(currState.first).getText()+" и " + massButtonElem.get(currState.second).getText());
                }
                else{
                    massButtonElem.get(currState.first).setStyle("-fx-background-color: blue");
                    massButtonElem.get(currState.second).setStyle("-fx-background-color: blue");
                    informationArea.setText("Этап 1. Построение кучи\nСравниваем элементы "+ massButtonElem.get(currState.first).getText()+" и "+ massButtonElem.get(currState.second).getText()+ "\n, но не меняем");
                }
            }
            case built -> {
                informationArea.setText("Этап 1. Построение кучи\nКуча построена");
            }
            case preSiftDown -> {
                massButtonElem.get(currState.first).setStyle("-fx-background-color: blue");
                massButtonElem.get(currState.second).setStyle("-fx-background-color: blue");
                informationArea.setText("Этап 2. Сортировка\nСравниваем элементы "+ massButtonElem.get(currState.first).getText()+" и "+ massButtonElem.get(currState.second).getText());
            }
            case siftDown -> {
                if(currState.isChanged) {
                    stepSwapButtons(currState.first, currState.second, "", "-fx-background-color: blue");
                    informationArea.setText("Этап 2. Сортировка\n поменяли элементы: "+ " "+ massButtonElem.get(currState.first).getText()+" и " + massButtonElem.get(currState.second).getText());
                }
                else {
                    massButtonElem.get(currState.first).setStyle("-fx-background-color: blue");
                    massButtonElem.get(currState.second).setStyle("-fx-background-color: blue");
                    informationArea.setText("Этап 2. Сортировка\nНе меняем элементы "+ massButtonElem.get(currState.first).getText()+" и "+ massButtonElem.get(currState.second).getText());
                }
            }
            case delMin -> {
                stepSwapButtons(currState.first, currState.second, "Этап 2. Сортировка\nудалили корень", "-fx-background-color: green");
                informationArea.setText("Этап 2. Сортировка\nудалили корень");
            }
            case done -> {
                informationArea.setText("Этап 2. Сортировка\nДанные отсортированы!");
                setAllButtonsStyle("-fx-background-color: green");
                saveButton.setDisable(false);
                insertButton.setDisable(false);
                loadButton.setDisable(false);
                stepButton.setDisable(true);
                runButton.setDisable(false);
                currState = null;
                countElem = 0;
                massButtonElem = null;
            }
            case initial -> {

            }
        }
        currState = mainwindow.stepSort(drawField);
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
        massButtonElem = new ArrayList<EditableButton>();
        for (int i = 0; i < sortData.length; i++) {
            massButtonElem.add(new EditableButton(Integer.toString(sortData[i])));
            elemBox.add(massButtonElem.get(i), i % MAX_ELEM_IN_ROW, i / MAX_ELEM_IN_ROW);
            GridPane.setHalignment(massButtonElem.get(i), HPos.CENTER);
            massButtonElem.get(i).setDisable(true);
        }
        countElem = 0;
        informationArea.setText("Данные\nотсортированы!");
        saveButton.setDisable(false);
        insertButton.setDisable(false);
        loadButton.setDisable(false);
        stepButton.setDisable(true);
        runButton.setDisable(false);
    }
    @FXML
    private void buttonInsertPressed() {
        stringToButtons(insertDataLine.getText());
        rezultButton.setDisable(false);
        saveButton.setDisable(true);
    }
    @FXML
    private void buttonSavePressed() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Save rezults");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(mainwindow.getPrimaryStage());
        if (file != null) {
            mainwindow.writeData(file);
        }
    }
    @FXML
    private void buttonLoadPressed() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Open file with source data");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(mainwindow.getPrimaryStage());
        String data = null;
        if (file != null) {
            data = mainwindow.readData(file);
        }
        if(data!= null){
            stringToButtons(data);
        }
    }
    @FXML
    private void buttonAuthorsPressed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Авторы");
        alert.setHeaderText(null);
        alert.setContentText("Данное приложение написали студенты ФКТИ ЛЭТИ:\nПтичкин Сергей, GUI" +
                "\nПрибылов Никита, Сборка, тестирование, визуализация алгоритма\nНоздрин Василий, " +
                "алгоритм сортировки, файловый ввод/вывод\n\nПреподаватель: Фирсов Михаил Александрович");
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
        saveButton.setDisable(true);
        loadButton.setDisable(false);
        insertButton.setDisable(false);
        stepButton.setDisable(true);
        runButton.setDisable(false);
        drawField.getChildren().clear();
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
            massButtonElem = new ArrayList<EditableButton>();
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
        private TextField tf;
        final ContextMenu contextMenu = new ContextMenu();

        public EditableButton(String text) {
            setText(text);
            tf = new TextField();
            MenuItem change = new MenuItem("Изменить");
            MenuItem swap = new MenuItem("Обмен");
            MenuItem delete = new MenuItem("Удалить");
            contextMenu.getItems().addAll(change, swap, delete);
            this.setContextMenu(contextMenu);

            // настройка контекстного меню
            change.setOnAction(e -> {
                tf.setText(getText());
                setGraphic(tf);
                setText("");
                tf.requestFocus();
                tf.selectAll();
            });

            swap.setOnAction(e -> {
                if (swapState == 0) {
                    first = this;
                    first.setStyle("-fx-background-color: blue");
                } else {
                    second = this;
                    if (first == second) {
                        swapState = 0;
                    } else {
                        swapEditableButtons(first, second);
                        first.setStyle("");
                    }
                }
                swapState = ++swapState % 2;
            });

            delete.setOnAction(e -> {
                massButtonElem.remove(this);
                elemBox.getChildren().remove(0, elemBox.getChildren().size());
                for (int i = 0; i < massButtonElem.size(); i++) {
                    elemBox.add(massButtonElem.get(i), i % MAX_ELEM_IN_ROW, i / MAX_ELEM_IN_ROW);
                    GridPane.setHalignment(massButtonElem.get(i), HPos.CENTER);
                }
                elemBox.add(addElemButton, countElem % MAX_ELEM_IN_ROW, countElem / MAX_ELEM_IN_ROW);
                countElem--;
            });

            // настройка текстового поля
            tf.textProperty().addListener((obs) ->
                    tf.setPrefWidth(0.5 * tf.getFont().getSize() * tf.getText().length() + 30));

            tf.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
                if (!"-0123456789".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            });

            tf.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) return;
                if (isStringCorrect(tf.getText())) {
                    setText(tf.getText());
                    setGraphic(null);
                } else {
                    new Alert(AlertType.ERROR, "Вы ввели некорректные данные.").showAndWait();
                    tf.requestFocus();
                    tf.selectAll();
                }
            });
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
            massButtonElem = new ArrayList<EditableButton>();
            elemBox.getChildren().remove(0, elemBox.getChildren().size());
            for (int i = 0; i < numbers.length; i++) {
                massButtonElem.add(new EditableButton(Integer.toString(numbers[i])));
                elemBox.add(massButtonElem.get(i), i % MAX_ELEM_IN_ROW, i / MAX_ELEM_IN_ROW);
                GridPane.setHalignment(massButtonElem.get(i), HPos.CENTER);
            }
            elemBox.add(addElemButton, countElem % MAX_ELEM_IN_ROW, countElem / MAX_ELEM_IN_ROW);
        } catch (NumberFormatException nfe) {
            new Alert(AlertType.ERROR, "Введены некорректные данные.").showAndWait();
        }
    }

    private void swapEditableButtons(EditableButton first, EditableButton second) {
        int firstRow = GridPane.getRowIndex(first);
        int firstCol = GridPane.getColumnIndex(first);
        int secondRow = GridPane.getRowIndex(second);
        int secondCol = GridPane.getColumnIndex(second);
        Collections.swap(massButtonElem, massButtonElem.indexOf(first), massButtonElem.indexOf(second));
        elemBox.getChildren().removeAll(first, second);
        elemBox.add(first, secondCol, secondRow);
        elemBox.add(second, firstCol, firstRow);
        System.out.println(massButtonElem);
    }

    private void clearStyleButtons(){
        for(EditableButton a: massButtonElem){
            if(a.getStyle().equals("-fx-background-color: blue")) {
                a.setStyle("");
            }
        }
    }

    private void setAllButtonsStyle(String style){
        for(EditableButton a: massButtonElem){
            a.setStyle(style);
        }
    }

    private void stepSwapButtons(int i1, int i2, String message, String color){
        int firstRow = i1 / elemBox.getColumnCount();
        int firstCol = i1 % elemBox.getColumnCount();
        int secondRow = i2 / elemBox.getColumnCount();
        int secondCol = i2 % elemBox.getColumnCount();
        Button first = massButtonElem.get(i1);
        Button second = massButtonElem.get(i2);
        Collections.swap(massButtonElem, i1, i2);
        elemBox.getChildren().removeAll(first, second);
        elemBox.add(first, secondCol, secondRow);
        elemBox.add(second, firstCol, firstRow);
        //informationArea.setText(message+ " "+ first.getText()+" и " + second.getText());
        if(color.equals("-fx-background-color: green")) {
            second.setStyle("-fx-background-color: green");
        }
        else if(color.equals("-fx-background-color: blue")){
            first.setStyle("-fx-background-color: blue");
            second.setStyle("-fx-background-color: blue");
        }
    }
}

