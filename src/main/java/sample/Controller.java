package sample;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {
    private MainWindow mainwindow;
    private ArrayList<EditableButton> massButtonElem = null;
    private int countElem = 0;
    private static int MAX_ELEM_IN_ROW = 35;
    private int swapState = 0;
    private EditableButton first, second;
    stepState currState = null;
    private Stage prevStepStage;
    private PrevStepController prevStepController;


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
    private Button prevStepButton;
    @FXML
    private ScrollPane scrPane;
    @FXML
    void initialize() {
        //создание дополнительного окна
        prevStepStage = new Stage();
        AnchorPane root = null;
        try{
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = Main.class.getResource("/prevStepWindow.fxml");
            loader.setLocation(xmlUrl);
            root = loader.load();
            prevStepController = loader.getController();
        } catch (Exception e) {
            System.exit(2);
        }
        prevStepStage.setTitle("Предыдущий шаг");
        prevStepStage.setScene(new Scene(root, 700, 400));

        scrPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (massButtonElem != null) {
                if (scrPane.getWidth() > 300) {
                    int sum = 0;
                    for (EditableButton b : massButtonElem) {
                        sum += b.getWidth();
                    }
                    int ar = sum / massButtonElem.size();
                    MAX_ELEM_IN_ROW = (int) (scrPane.getWidth() / (ar * 1.05));
                    elemBox.getChildren().clear();
                    if (currState == null) {
                        elemBox.getChildren().add(addElemButton);
                    }
                    for (int i = 0; i < massButtonElem.size(); i++) {
                        GridPane.setHalignment(massButtonElem.get(i), HPos.CENTER);
                        elemBox.getChildren().remove(addElemButton);
                        elemBox.add(massButtonElem.get(i), i % MAX_ELEM_IN_ROW,i / MAX_ELEM_IN_ROW);
                        if (currState == null) {
                            elemBox.add(addElemButton, (i + 1) % MAX_ELEM_IN_ROW, (i + 1) / MAX_ELEM_IN_ROW);
                        }
                    }
                }
            }
        });
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
        prevStepButton.setDisable(false);
        loadButton.setDisable(true);
        insertButton.setDisable(true);
        runButton.setDisable(true);
        currState = null;
        buttonStepPressed();
    }
    @FXML
    private void buttonStepPressed() {
        if(currState==null){
            currState = new stepState();
            currState.state = WeakHeap.State.initial;
            currState.first = 0;currState.second = 0;
            currState.length = 0;
        }
        prevStepController.setPrevStepState(massButtonElem, informationArea.getText(), drawField.getChildren());
        clearStyleButtons();
        first = massButtonElem.get(currState.first);
        second = massButtonElem.get(currState.second);
        switch (currState.state) {
            case preBuilding -> {
                first.setTextFill(Paint.valueOf("WHITE"));
                second.setTextFill(Paint.valueOf("WHITE"));
                first.setStyle("-fx-background-color: blue");
                second.setStyle("-fx-background-color: blue");
                informationArea.setText("Этап 1. Построение кучи\nСравниваем элементы:\n"+ second.getText()+" и " + first.getText());
            }
            case building-> {
                if(currState.isChanged){
                    swapEditableButtons(first, second,"-fx-background-color: orange");
                    first.setTextFill(Paint.valueOf("WHITE"));
                    second.setTextFill(Paint.valueOf("WHITE"));
                    informationArea.setText("Этап 1. Построение кучи\nМеняем элементы:\n"+ second.getText()+" и " + first.getText()
                            + "\nМеняем бит у элемента:\n" + second.getText());
                }
                else{
                    first.setStyle("-fx-background-color: blue");
                    second.setStyle("-fx-background-color: blue");
                    first.setTextFill(Paint.valueOf("WHITE"));
                    second.setTextFill(Paint.valueOf("WHITE"));
                    informationArea.setText("Этап 1. Построение кучи\nНе меняем элементы:\n"+ second.getText()+" и "+ first.getText());
                }
            }
            case built -> {
                informationArea.setText("Этап 1. Построение кучи\nКуча построена");
            }
            case preSiftDown -> {
                first.setStyle("-fx-background-color: yellow");
                second.setStyle("-fx-background-color: yellow");
                informationArea.setText("Этап 2. Сортировка\nСравниваем элементы:\n"+ second.getText()+" и "+ first.getText());
            }
            case siftDown -> {
                if(currState.isChanged) {
                    swapEditableButtons(first, second, "");
                    informationArea.setText("Этап 2. Сортировка\nМеняем элементы:\n"+ second.getText()+" и " + first.getText()
                            + "\nМеняем бит у элемента\n" + second.getText());
                }
                else {
                    informationArea.setText("Этап 2. Сортировка\nНе меняем элементы:\n"+ second.getText()+" и "+ first.getText());
                }
            }
            case delMin -> {
                swapEditableButtons(first, second, "-fx-background-color: green");
                informationArea.setText("Этап 2. Сортировка\nУдаляем корень\nСтавим на место корня\nпоследний элемент: " + first.getText());
            }
            case done -> {
                informationArea.setText("Этап 2. Сортировка\nДанные отсортированы!");
                setAllButtonsStyle("-fx-background-color: green");
                saveButton.setDisable(false);
                insertButton.setDisable(false);
                loadButton.setDisable(false);
                stepButton.setDisable(true);
                prevStepButton.setDisable(true);
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
    void prevStepButtonPressed() {
        prevStepStage.show();
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
        setAllButtonsStyle("-fx-background-color: green");
        saveButton.setDisable(false);
        insertButton.setDisable(false);
        loadButton.setDisable(false);
        stepButton.setDisable(true);
        prevStepButton.setDisable(true);
        runButton.setDisable(false);
        drawField.getChildren().clear();
    }
    @FXML
    private void buttonInsertPressed() {
        try {
            stringToButtons(insertDataLine.getText());
        } catch (NumberFormatException nfe) {
            new Alert(AlertType.ERROR, "Введены некорректные данные.").showAndWait();
        }
        rezultButton.setDisable(false);
        saveButton.setDisable(false);
    }
    @FXML
    private void buttonSavePressed() {
        if(massButtonElem == null)
            return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Save data");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(mainwindow.getPrimaryStage());
        int[] data = new int[massButtonElem.size()];
        for(Button a : massButtonElem ){
            data[massButtonElem.indexOf(a)] = Integer.valueOf(a.getText());
        }
        if (file != null) {
            mainwindow.writeData(file, data);
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
        if(data != null){
            try {
                stringToButtons(data);
            } catch (NumberFormatException nfe) {
                new Alert(AlertType.ERROR, "Введены некорректные данные.").showAndWait();
            }
        }
    }
    @FXML
    private void buttonAuthorsPressed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Авторы");
        alert.setHeaderText(null);
        alert.setContentText("""
                Данное приложение написали студенты ФКТИ ЛЭТИ:
                Птичкин Сергей: GUI, файловый ввод/вывод
                Прибылов Никита: визуализация алгоритма, тестирование, сборка
                Ноздрин Василий: алгоритм сортировки

                Преподаватель: Фирсов Михаил Александрович""");
        alert.getDialogPane().setMinWidth(550);
        alert.setResizable(true);
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
        prevStepButton.setDisable(true);
        runButton.setDisable(false);
        drawField.getChildren().clear();
        currState = null;
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
        saveButton.setDisable(false);
    }
    @FXML
    private void buttonHelpPressed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Справка");
        alert.setHeaderText(null);
        alert.setContentText("""
                Данное приложение визуализирует алгоритм сортировки слабой кучей.

                Ввод данных:
                Можно ввести данные через пробел в текстовую линию, затем нажать клавишу "ввод".
                Можно добавлять элементы непосредственно в массив нажимая клавишу "+" и меняя значение элемента через контекстное меню кнопки.
                Контекстное меню открывается нажатием ПКМ на кнопку элемента. По умолчанию значение элемента - 0.
                Также можно удалять элементы и менять местами при нажатии соответствующей опции в меню.
                Свап элементов происходит при выборе опции у двух элементов. Первый подсвечивается синим цветом.

                Алгоритм:
                Запуск алгоритма происходит при нажатии кнопки "Запуск".
                Можно получить мгновенный результат, нажав кнопку "Результаты". Результат отобразится в массиве кнопок.
                Для пошагового выполнения алгоритма можно использовать кнопку "Шаг".\s
                Также есть возможность посмотреть на состояние выполнения алгоритма на предыдущем шаге, нажав кнопку "Предыдущий шаг".
                На каждом шаге в текстовом поле справа описывается, что происходит на текущем шаге.

                Визуализация алгоритма:
                Текущее состояние массива данных отражается в панели с кнопками, на которых отражается значение каждого элемента.
                При выполнении алгоритма кнопки могут поменять свой порядок расположения, что отражает изменения в массиве данных.
                В нижней части окна рисуется текущее состояние бинарного дерева кучи.\s
                Под вершинами также указывается индекс элемента и значение бита.
                При сравнении, перемещении и удалении элементов они подсвечиваются специальным цветом.
                Отсортированная часть массива принимает зелёный цвет, из кучи же они удаляются.\s

                Сохранение:
                При нажатии кнопки "Сохранить" будет выведено окно предлагающее сохранить данные в файл.
                Данные, которые записываются в файл - текущее состояние массива данных.

                Загрузка:
                При нажатии кнопки "Загрузка" будет выведено окно, предлагающее считать данные из файла с расширением ".txt".
                В файле должны быть данные, представленные символами чисел, разделённых исключительно пробелами.
                Если будет введён отличный от пробела и цифр символ, данные не будут считаны.""");
        alert.setResizable(true);
        alert.getDialogPane().setMinWidth(1000);
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
                        swapEditableButtons(first, second, "");
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

    private void stringToButtons(String str) throws NumberFormatException {
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
    }

    private void swapEditableButtons(EditableButton first, EditableButton second, String color) {
        int firstRow = GridPane.getRowIndex(first);
        int firstCol = GridPane.getColumnIndex(first);
        int secondRow = GridPane.getRowIndex(second);
        int secondCol = GridPane.getColumnIndex(second);
        Collections.swap(massButtonElem, massButtonElem.indexOf(first), massButtonElem.indexOf(second));
        elemBox.getChildren().removeAll(first, second);
        elemBox.add(first, secondCol, secondRow);
        elemBox.add(second, firstCol, firstRow);
        if(color.equals("-fx-background-color: green")) {
            second.setStyle(color);
            second.setTextFill(Paint.valueOf("WHITE"));
        }
        else {
            first.setStyle(color);
            second.setStyle(color);
        }
    }

    private void clearStyleButtons(){
        for(EditableButton a: massButtonElem){
            if(!a.getStyle().equals("-fx-background-color: green")) {
                a.setStyle("");
                a.setTextFill(Paint.valueOf("BLACK"));
            }
        }
    }

    private void setAllButtonsStyle(String style){
        for(EditableButton a: massButtonElem){
            a.setStyle(style);
            a.setTextFill(Paint.valueOf("WHITE"));
        }
    }

}