import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

public class WeakHeap {
    int length;
    int[] values;
    int[] bits;
    // to add element -> add to the bottom and siftUp
    // to remove element -> swap with last and siftDown

    WeakHeap(@org.jetbrains.annotations.NotNull Integer[] data) {
        this.length = data.length;
        this.values = new int[length];
        this.bits = new int[length];
        for (int i = 0; i < length; i++) {
            this.values[i] = data[i];
            this.bits[i] = 0;
        }
        this.build();
    }

    @Override
    public String toString() {
        if (length < 1)
            return null;
        StringBuilder result = new StringBuilder();
        LinkedList<Integer> row = new LinkedList<>();
        LinkedList<Integer> new_row = new LinkedList<>();
        int lineSize = 64;
        int cellSize = lineSize;
        int height = 1; // could be 1+lb(length-1)
        for(int i = this.length-1; i > 0; i /= 2) {
            height++;
        }

        result.append(StringUtils.center(""+values[0], lineSize/2));
        row.addLast(1);
        for(int level = 1; level <= height; level++) { // add each layer
            result.append("\n");
            while (!(row.isEmpty())) {
                Integer node = row.pollFirst();
                if (node == null || !(node < length)) {
                    result.append(" ".repeat(cellSize));
                    new_row.addLast(null);
                    new_row.addLast(null);
                } else {
                    result.append(StringUtils.center(""+values[node], cellSize));
                    new_row.addLast(2*node+bits[node]);     // add Left child
                    new_row.addLast(2*node+1-bits[node]);   // add right child
                }
            }
            row = new_row;
            new_row = new LinkedList<>();
            cellSize /= 2;
        }
        return result.toString();
    }

    public boolean join(int v, int w) {
        if (values[v] < values[w]) {
            // swap values
            this.values[v] ^= this.values[w];
            this.values[w] ^= this.values[v];
            this.values[v] ^= this.values[w];
            // change bit
            bits[v] = 1 - bits[v];
            return true;
        }
        return false;
    } // проталкивает минимум из v наверх в w

    public int up(int v) {
        if (((v%2)^(bits[v/2])) == 1)
            return v / 2;
        return up(v / 2);
    } // returns Right parent

    void siftUp(int v) {
        int w;
        while (this.join(v, w = up(v)))
            v = w;
    } // sifts WeakHeap

    int siftDown() {
        if (length < 1) {
            return values[0];
        }
        int min = values[0];                        // save min
        length--;                                   // delete min
        this.values[0] ^= this.values[length];      // swap values
        this.values[length] ^= this.values[0];      // .
        this.values[0] ^= this.values[length];      // .
        if (length < 2)                             // 1-node case (only root)
            return min;                             // .
        int v = 1;                                  // greedy go down to the left
        while (2 * v + bits[v] < length)            // .
            v = 2 * v + bits[v];                    // .
        while (v > 0) {                             // siftDown
            join(v, 0);                          // .
            v /= 2;                                 // .
        }
        return min;
    } // Deletes minimum

    public void build() {
        for (int i = length - 1; i > 0; i--)
            this.join(i, this.up(i));
    } // makes WeakHeap a correct one

    void heapsort() {
        for (int k = 1; k < values.length; k++) {
            this.siftDown();
        }
        length = values.length;
    }
}
