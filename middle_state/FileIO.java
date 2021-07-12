package sample;

import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileIO {
    private File resource;
    public FileIO(File resource){
        this.resource = resource;
    }
    public void writeToFile(int[] data){
        try (FileWriter dataWriter = new FileWriter(resource, true);){
            for(int i = 0; i< data.length; i++){
                dataWriter.write(String.valueOf(data[i]+" "));
            }
            dataWriter.write("\n");
        }
        catch (IOException e){
            new Alert(Alert.AlertType.ERROR, "Ошибка при записи в файл!").showAndWait();
        }
    }
    public String readFromFile(){
        String data = null;
        try {
            data = new String(Files.readAllBytes(Paths.get(resource.getPath())));
        }
        catch (IOException e){
            new Alert(Alert.AlertType.ERROR, "Ошибка при считывании из файла!").showAndWait();
        }
        return data;
    }
}
