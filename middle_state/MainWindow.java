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
    private WeakHeap stepHeap;
    private WeakHeap.State prevState;
    private stepState currState;


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
        stepHeap = new WeakHeap(data);
        stepHeap.build();
        stepHeap.heapsort();
        return stepHeap.values;
    }

    public void startStepSort(Integer[] data, AnchorPane drawField){
        stepHeap = null;
        stepHeap = new WeakHeap(data);
        currState = new stepState();
        WeakHeapRenderer.render(stepHeap, drawField, List.of(), Paint.valueOf("RED"));
    }

    public stepState stepSort(AnchorPane drawField){
        currState.state = stepHeap.state;
        switch (currState.state) {
            case preBuilding, building -> {
                currState.first = stepHeap.joinId1;
                currState.second = stepHeap.joinId2;
                WeakHeapRenderer.render(stepHeap, drawField, List.of(currState.first, currState.second),
                        currState.isChanged ? Paint.valueOf("ORANGE") : Paint.valueOf("BLUE"),
                        currState.isChanged ? stepHeap.allChildrenOf(currState.first) : List.of(),
                        Paint.valueOf("LIGHTBLUE"));
                currState.isChanged = stepHeap.buildStep(); // made a step
                currState.first = stepHeap.joinId1;
                currState.second = stepHeap.joinId2;
                prevState = currState.state;
            }
            case built -> {
                currState.first = stepHeap.joinId1;
                currState.second = stepHeap.joinId2;
                WeakHeapRenderer.render(stepHeap, drawField,
                        List.of(currState.first, currState.second),
                        currState.isChanged ? Paint.valueOf("ORANGE") : Paint.valueOf("BLUE"),
                        currState.isChanged ? stepHeap.allChildrenOf(currState.first) : List.of(),
                        Paint.valueOf("LIGHTBLUE"));
                currState.isChanged = stepHeap.heapsortStep(); // made a step
                prevState = currState.state;
            }
            case delMin -> {
                currState.first = stepHeap.length - 1;
                currState.second = stepHeap.joinId2;
                WeakHeapRenderer.render(stepHeap, drawField, List.of(0), Paint.valueOf("PURPLE"));
                currState.isChanged = stepHeap.heapsortStep(); // made a step
                prevState = currState.state;
            }
            case preSiftDown -> {
                if (prevState == WeakHeap.State.delMin) {
                    System.out.println("prev state = delmin");
                    WeakHeapRenderer.render(stepHeap, drawField);
                    currState.first = stepHeap.joinId1;
                    currState.second = stepHeap.joinId2;
                } else {
                    currState.first = stepHeap.joinId1;
                    if (currState.first != currState.first / 2 * 2 + stepHeap.bits[currState.first / 2]) {
                        currState.first = currState.first / 2 * 2 + stepHeap.bits[currState.first / 2];
                    }
                    currState.second = 0;
                    WeakHeapRenderer.render(stepHeap, drawField,
                            List.of(currState.first, currState.second),
                            currState.isChanged ? Paint.valueOf("PINK") : Paint.valueOf("YELLOW"),
                            currState.isChanged ? stepHeap.allChildrenOf(currState.first) : List.of(),
                            Paint.valueOf("AQUA"));
                }
                prevState = currState.state;
                currState.isChanged = stepHeap.heapsortStep();
                currState.first = stepHeap.joinId1;
                currState.second = stepHeap.joinId2;
            }
            case siftDown -> {
                if (prevState == WeakHeap.State.delMin) {
                    System.out.println("prev state = delmin");
                    WeakHeapRenderer.render(stepHeap, drawField);
                    currState.first = stepHeap.joinId1;
                    currState.second = stepHeap.joinId2;
                } else {
                    currState.first = stepHeap.joinId1;
                    currState.second = 0;
                    WeakHeapRenderer.render(stepHeap, drawField,
                            List.of(currState.first, currState.second),
                            currState.isChanged ? Paint.valueOf("PINK") : Paint.valueOf("YELLOW"),
                            currState.isChanged ? stepHeap.allChildrenOf(currState.first) : List.of(),
                            Paint.valueOf("AQUA"));
                    prevState = currState.state;
                    currState.isChanged = stepHeap.heapsortStep(); // made a step
                }
            }
            case done -> {
                WeakHeapRenderer.render(stepHeap, drawField);
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
    public void writeData(File destination, int[] data){
        FileIO saveFile = new FileIO(destination);
        saveFile.writeToFile(data);
    }
}
