package sample;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;

public class WeakHeapSteps extends WeakHeap {
    public enum State {
        delMin,
        siftDown,
        done
    }
    public State state;
    public int swapId;
    int initialLength;
    private GridPane elemBox;
    private TextArea informationArea;
    private ArrayList<Controller.EditableButton> massButtonElem;
    private GridPane drawField;

    WeakHeapSteps(Integer[] data, GridPane elemBox, TextArea informationArea, ArrayList<Controller.EditableButton> massButtonElem) {
        super(data);
        this.massButtonElem = massButtonElem;
        this.elemBox = elemBox;
        this.informationArea = informationArea;
        state = State.delMin;
        swapId = 0;
        initialLength = length;
    }

    public void step() {
        switch (state) {
            case delMin -> {
                if (length < 1) {
                    state = State.done;
                    break;
                }
                length--;
                //stepSwapButtons(0, length, "Delete", "-fx-background-color: green"); + рисовалка
                this.values[0] ^= this.values[length];
                this.values[length] ^= this.values[0];
                this.values[0] ^= this.values[length];
                swapId = 1;
                while (2*swapId+bits[swapId] < length)
                    swapId = 2*swapId+bits[swapId];     // left Child
                swapId *= 2;    // !
                state = State.siftDown;
                if (length == 1) {
                    state = State.done;
                }
            }
            case siftDown -> {
                swapId /= 2;    // !
                if (swapId > 0) {
                    join(swapId, 0);
                    //stepSwapButtons(swapId, 0, "проталкивание", "-fx-background-color: blue"); + рисовалка
                    // !
                } else {
                    state = State.delMin;
                }
            }
            default -> length = initialLength;
        }
    }

    @Override
    void heapsort() {
        while (!state.equals(State.done))
            step();
        length = initialLength;
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
        informationArea.setText(message);
        if(color.equals("-fx-background-color: green")) {
            first.setStyle("-fx-background-color: green");
        }
        else if(color.equals("-fx-background-color: blue")){
            first.setStyle("-fx-background-color: blue");
            second.setStyle("-fx-background-color: blue");
        }
    }
}
