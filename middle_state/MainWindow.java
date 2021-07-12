package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;

public final class MainWindow {
    private Stage primaryStage;
    private AnchorPane root;
    private WeakHeap heap;
    private WeakHeap stepHeap;

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

    public void startStepSort(Integer[] data, AnchorPane drawField){
        stepHeap = new WeakHeap(data);
        WeakHeapRenderer.render(stepHeap, drawField, List.of(), Paint.valueOf("RED"));
    }

    public stepState stepSort(AnchorPane drawField){
        stepState currState = new stepState();
        currState.state = stepHeap.state;
        if(currState.state == WeakHeap.State.preBuilding || currState.state == WeakHeap.State.building){
            currState.isChanged = stepHeap.buildStep();
        }
        else {
            currState.isChanged = stepHeap.heapsortStep();
        }
        currState.length = stepHeap.length;
        switch (currState.state){
            case preBuilding -> {
                if(!currState.isChanged){
                    stepSort(drawField);
                }
                else {
                    currState.first = stepHeap.joinId1;
                    currState.second = stepHeap.joinId2;
                }
            }
            case building, built-> {
                currState.first = stepHeap.joinId1;
                currState.second = stepHeap.joinId2;
            }
            case preSiftDown -> {
                System.out.println(currState.first = stepHeap.joinId1);
                currState.first = stepHeap.joinId1;
                currState.second = 0;
            }
            case siftDown-> {
                System.out.println(currState.first = stepHeap.joinId1);
                currState.first = stepHeap.joinId1;
                currState.second = 0;
            }
            case delMin -> {
                currState.first = stepHeap.length - 1;
                currState.second = 0;
            }
            case done -> {

            }
        }
        return currState;
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
