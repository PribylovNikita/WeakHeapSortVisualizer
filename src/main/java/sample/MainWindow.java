package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class MainWindow {
    private Stage primaryStage;
    private AnchorPane root;
    private WeakHeap heap;
    private WeakHeapSteps stepHeap;

    public MainWindow(Stage stage) {
        this.primaryStage = stage;
        try{
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = Main.class.getResource("sample.fxml");
            loader.setLocation(xmlUrl);
            root = loader.load();
            Controller appController = loader.getController();
            appController.setMainWindow(this);
        } catch (Exception e) {
            System.exit(0);
        }
        primaryStage.setTitle("Сортировка слабой кучей(v:1)");
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }
    public int[] sort(Integer[] data){
        heap = new WeakHeap(data);
        heap.build();
        heap.heapsort();
        return heap.values;
    }

    public void startStepSort(Integer[] data, AnchorPane drawField, GridPane elemBox, TextArea informationArea, ArrayList<Controller.EditableButton> massButtonElem){
        stepHeap = new WeakHeapSteps(data,elemBox, informationArea, massButtonElem);
        WeakHeapRenderer.render(stepHeap, drawField, List.of(), Paint.valueOf("RED"));
    }

    public void stepSort(AnchorPane drawField){
        stepHeap.step();
        WeakHeapRenderer.render(stepHeap, drawField, List.of(), Paint.valueOf("RED"));
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public String readData(File source){
        FileIO loadFile = new FileIO(source);
        return loadFile.readFromFile();
    }
    public void writeData(File destination){
        FileIO saveFile = new FileIO(destination);
        saveFile.writeToFile(heap.values);
    }
}
