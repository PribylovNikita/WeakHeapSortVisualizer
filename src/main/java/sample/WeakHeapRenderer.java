package sample;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;

public class WeakHeapRenderer {
    private static final double radius = 20;
    private static final double topLeftX = 3*radius, topLeftY = 3*radius;
    private static final Paint defaultColor = Paint.valueOf("IVORY");

    public static void render(WeakHeap wh, AnchorPane drawField,
                              List<Integer> actionNodes, Paint actionColor,
                              List<Integer> childrenOfActionNodes, Paint actionColorForChildren) {
        if (wh == null || drawField == null) return;
        if (wh.length < 1) {
            drawField.getChildren().clear();
            return;
        }
        if (actionNodes == null) actionNodes = List.of();
        if (actionColor == null) actionColor = defaultColor;
        if (childrenOfActionNodes == null) childrenOfActionNodes = List.of();
        if (actionColorForChildren == null) actionColorForChildren = defaultColor;

        drawField.getChildren().clear();
        Line line;
        Text text;
        Circle circle;

        // draw root
        circle = new Circle(topLeftX, topLeftY, radius, actionNodes.contains(0) ? actionColor : defaultColor);
        drawField.getChildren().add(circle);

        text = new Text(topLeftX, topLeftY+3, Integer.toString(wh.values[0]));
        text.setX(text.getX() - text.getLayoutBounds().getWidth()/2);
        if (circle.getFill() == Paint.valueOf("BLUE")) {
            text.setFill(Paint.valueOf("WHITE"));
        }
        drawField.getChildren().add(text);

        text = new Text(topLeftX, topLeftY+3 + radius*1.5, 0 + " (" + wh.bits[0] + ")");
        text.setX(text.getX() - text.getLayoutBounds().getWidth()/2);
        text.setFont(Font.font(10));
        drawField.getChildren().add(text);

        // prep for drawing the rest
        LinkedList<Integer> row = new LinkedList<>();
        LinkedList<Integer> new_row = new LinkedList<>();
        int length = wh.length;
        int height = 1 + (int) (Math.log(length) / Math.log(2));
        double curX;
        double curY = topLeftY;
        double cellWidth = 2*radius * length;
        double cellHeight = 4*radius;

        row.addLast(1);
        for(int level = 1; level <= height; level++) { // add each layer
            curY += cellHeight;
            curX = topLeftX-cellWidth/2;
            while (!(row.isEmpty())) {
                Integer node = row.pollFirst();
                curX += cellWidth;
                if (node == null || !(node < length)) {
                    new_row.addLast(null);
                    new_row.addLast(null);
                } else {
                    // draw line from child to parent
                    line = new Line(curX, curY, (wh.bits[node/2] + node%2) % 2 == 0 ? curX+cellWidth/2 : curX-cellWidth/2, curY-cellHeight);
                    drawField.getChildren().add(line);
                    line.toBack();

                    // draw child
                    circle = new Circle(curX, curY, radius,
                            actionNodes.contains(node) ? actionColor :
                                    childrenOfActionNodes.contains(node) ? actionColorForChildren : defaultColor);
                    drawField.getChildren().add(circle);

                    // draw value

                    text = new Text(curX,curY+3, Integer.toString(wh.values[node]));
                    text.setX(text.getX() - text.getLayoutBounds().getWidth()/2);
                    if (circle.getFill() == Paint.valueOf("BLUE")) {
                        text.setFill(Paint.valueOf("WHITE"));
                    }
                        drawField.getChildren().add(text);

                    // draw index + bit
                    text = new Text(curX, curY+3 + radius*1.5, node + " (" + wh.bits[node] + ")");
                    text.setX(text.getX() - text.getLayoutBounds().getWidth()/2);
                    text.setFont(Font.font(10));
                    drawField.getChildren().add(text);

                    // prep next children
                    new_row.addLast(2 * node + wh.bits[node]); // add Left child
                    new_row.addLast(2 * node + 1 - wh.bits[node]); // add right child
                }
            }
            row = new_row;
            new_row = new LinkedList<>();
            cellWidth /= 2;
        }

    }

    public static void render(WeakHeap wh, AnchorPane drawField, List<Integer> actionNodes, Paint actionColor) {
        render(wh, drawField, actionNodes, actionColor, List.of(), defaultColor);
    }

    public static void render(WeakHeap wh, AnchorPane drawField) {
        render(wh, drawField, List.of(), defaultColor, List.of(), defaultColor);
    }

}