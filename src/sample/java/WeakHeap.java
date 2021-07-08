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
        this.Build();
    }

    @Override
    public String toString() {
        if (length < 1)
            return null;
        StringBuilder result = new StringBuilder();
        LinkedList<Integer> row = new LinkedList<>();
        LinkedList<Integer> new_row = new LinkedList<>();

        result.append(String.format("%5d", values[0]));
        row.addLast(0);

        int height = 0; // could be 1+lb(length-1)
        for(int i = this.length-1; i > 0; i /= 2) {
            height++;
        }

        for(int i = 0; i < height; i++) { // add each layer
            while (!(row.isEmpty())) {
                Integer node = row.pollFirst();
//                result.append(String.format("%5d", values[node]));
            }
        }

        return result.toString();
    }

    public boolean Join(int v, int w) {
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

    public int Up(int v) {
        if (((v%2)^(bits[v/2])) == 1)
            return v / 2;
        return Up(v / 2);
    } // returns Right parent

    void SiftUp(int v) {
        int w;
        while (this.Join(v, w = Up(v)))
            v = w;
    } // sifts WeakHeap

    int SiftDown() {
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
        while (v > 0) {                             // SiftDown
            Join(v, 0);                          // .
            v /= 2;                                 // .
        }
        return min;
    } // Deletes minimum


    public void Build() {
        for (int i = length - 1; i > 0; i--)
            this.Join(i, this.Up(i));
    } // makes WeakHeap a correct one

    void heapsort() {
        for (int k = 1; k < values.length; k++) {
            this.SiftDown();
        }
        length = values.length;
    }
}
