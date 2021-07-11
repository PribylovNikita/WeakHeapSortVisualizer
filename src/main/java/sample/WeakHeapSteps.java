package sample;

public class WeakHeapSteps extends WeakHeap {
    public enum State {
        delMin,
        siftDown,
        done
    }
    public State state;
    public int swapId;
    int initialLength;

    WeakHeapSteps(Integer[] data) {
        super(data);
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
                    // !
                } else {
                    state = State.delMin;
                }
            }
            default -> length = initialLength;
        }
    }
}
