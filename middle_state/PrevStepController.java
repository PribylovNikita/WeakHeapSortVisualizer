package sample;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class PrevStepController {
    private ArrayList<Button> prevMassButtonElem = null;
    private static int MAX_ELEM_IN_ROW = 35;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane prevDrawField;

    @FXML
    private TextArea prevInformationArea;

    @FXML
    private ScrollPane prevScrollPane;

    @FXML
    private GridPane prevElemBox;

    @FXML
    void initialize() {
        prevScrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (prevMassButtonElem != null) {
                if (prevScrollPane.getWidth() > 300) {
                    int sum = 0;
                    for (Button b : prevMassButtonElem) {
                        sum += b.getWidth();
                    }
                    int ar = sum / prevMassButtonElem.size();
                    MAX_ELEM_IN_ROW = (int) (prevScrollPane.getWidth() / (ar * 1.05));
                    prevElemBox.getChildren().clear();
                    for (int i = 0; i < prevMassButtonElem.size(); i++) {
                        GridPane.setHalignment(prevMassButtonElem.get(i), HPos.CENTER);
                        prevElemBox.add(prevMassButtonElem.get(i), i % MAX_ELEM_IN_ROW,i / MAX_ELEM_IN_ROW);
                    }
                }
            }
        });
    }

    public void setPrevStepState(ArrayList<Controller.EditableButton> massButtonElem, String textAreaMessage, ObservableList<Node> list){
        prevInformationArea.setText(textAreaMessage);
        prevMassButtonElem = new ArrayList<>();
        prevElemBox.getChildren().clear();
        Button nextButton;
        if(massButtonElem!= null) {
            for (int i = 0; i < massButtonElem.size(); i++) {
                nextButton = new Button();
                nextButton.setText(massButtonElem.get(i).getText());
                nextButton.setStyle(massButtonElem.get(i).getStyle());
                nextButton.setTextFill(massButtonElem.get(i).getTextFill());
                prevMassButtonElem.add(nextButton);
                prevElemBox.add(nextButton, i % MAX_ELEM_IN_ROW, i / MAX_ELEM_IN_ROW);
                GridPane.setHalignment(nextButton, HPos.CENTER);
            }
        }
        WeakHeapRenderer.clear(prevDrawField);
        prevDrawField.getChildren().addAll(list);
    }
}