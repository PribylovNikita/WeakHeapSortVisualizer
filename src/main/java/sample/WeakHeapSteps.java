package sample;

public class WeakHeapSteps extends WeakHeap {
    public enum State {
        building,
        built,
        delMin,
        siftDown,
        done
    }
    public State state;
    public int swapId;
    public int buildId;
    int initialLength;
    int buildIterator;

    WeakHeapSteps(Integer[] data) {
        super(data);
        this.buildIterator = length - 1;
        state = State.building;
        swapId = 0;
        initialLength = length;
    }

    public boolean buildStep() {
        boolean swapped = false;
        switch (state) {
            case delMin, siftDown, built -> {}
            case building -> {
                swapId = buildIterator;
                buildId = this.up(buildIterator);
                swapped = this.join(swapId, buildId);
                buildIterator--;
                if (buildIterator < 1)
                    this.state = State.built;
            }
        }
        return swapped;
    }

    @Override
    public void build() {
        while (!state.equals(State.built))
            buildStep();
    }

    public boolean step() {
        boolean swapped = false;
        switch (state) {
            case building -> {}
            case delMin, built -> {
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
                    swapped = join(swapId, 0);
                    // !
                } else {
                    state = State.delMin;
                }
            }
            default -> length = initialLength;
        }
        return swapped;
    }

    @Override
    public void heapsort() {
        while (!state.equals(State.done))
            step();
        length = initialLength;
    }
}
