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
    private static final double topLeftX = 4*radius, topLeftY = 4*radius;

    public static void render(WeakHeap wh, AnchorPane drawField,
                              List<Integer> actionNodes, Paint actionColor,
                              List<Integer> childrenOfActionNodes, Paint actionColorForChildren) {
//System.out.println("RENDER");
        if (wh.length < 1) return;
        if (actionNodes == null) actionNodes = List.of();
        if (childrenOfActionNodes == null) childrenOfActionNodes = List.of();

        drawField.getChildren().removeAll(drawField.getChildren());
        Paint color = Paint.valueOf("IVORY");
        Line line;
        Text text = new Text(topLeftX, topLeftY+3, Integer.toString(wh.values[0]));
        text.setX(text.getX() - text.getLayoutBounds().getWidth()/2);
        Circle circle = new Circle(topLeftX, topLeftY, radius, actionNodes.contains(0) ? actionColor : color);

        drawField.getChildren().add(circle);
        drawField.getChildren().add(text);

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
                    line = new Line(curX, curY, (wh.bits[node/2] + node%2) % 2 == 0 ? curX+cellWidth/2 : curX-cellWidth/2, curY-cellHeight);
                    drawField.getChildren().add(line);
                    line.toBack();

                    drawField.getChildren().add(new Circle(curX, curY, radius,
                            actionNodes.contains(node) ? actionColor :
                                    childrenOfActionNodes.contains(node) ? actionColorForChildren : color));

                    text = new Text(curX,curY+3, Integer.toString(wh.values[node]));
                    text.setX(text.getX() - text.getLayoutBounds().getWidth()/2);
                    drawField.getChildren().add(text);

                    text = new Text(curX-3, curY+3 + radius*1.5, Integer.toString(node));
                    text.setFont(Font.font(10));
                    drawField.getChildren().add(text);

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
        render(wh, drawField, actionNodes, actionColor, List.of(), Paint.valueOf("BLACK"));
    }

    public static void render(WeakHeap wh, AnchorPane drawField) {
        render(wh, drawField, List.of(), Paint.valueOf("BLACK"), List.of(), Paint.valueOf("BLACK"));
    }

}