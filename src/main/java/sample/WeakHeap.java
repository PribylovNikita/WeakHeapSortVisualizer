package sample;
import java.util.LinkedList;

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
