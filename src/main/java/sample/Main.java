package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private MainWindow mainwindow;
    @Override
    public void start(Stage primaryStage) throws Exception{
        mainwindow = new MainWindow(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}