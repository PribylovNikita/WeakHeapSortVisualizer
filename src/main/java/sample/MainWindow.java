package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;

public final class MainWindow {
    private Stage primaryStage;
    private AnchorPane root;
    private WeakHeapSteps heap;
    public MainWindow(Stage stage) {
        this.primaryStage = stage;
        try{
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = Main.class.getResource("/sample.fxml");
            loader.setLocation(xmlUrl);
            root = loader.load();
            Controller appController = loader.getController();
            appController.setMainWindow(this);
        } catch (Exception e) {
            System.exit(0);
        }
        primaryStage.setTitle("Прототип");
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }
    public int[] sort(Integer[] data){
        heap = new WeakHeapSteps(data);
        while (!heap.state.equals(WeakHeapSteps.State.done))
            heap.step();
        heap.heapsort();
        return heap.values;
    }
}
