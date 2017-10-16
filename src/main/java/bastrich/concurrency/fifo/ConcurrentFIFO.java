package bastrich.concurrency.fifo;

/**
 * @author bastrich on 13.10.2017.
 */
public class ConcurrentFIFO<T> {

    private T[] storage;
    private int lastAddedIndex;
    private int lastPolledIndex;

    public ConcurrentFIFO(int size) {
        storage = (T[]) new Object[size];
        lastAddedIndex = -1;
        lastPolledIndex = -1;
    }

    public synchronized boolean add(T item) {
        if (lastAddedIndex + 1 == lastPolledIndex
                || lastAddedIndex == storage.length - 1 && lastPolledIndex == 0) {
            return false;
        }

        lastAddedIndex = lastAddedIndex == storage.length - 1 ? 0 : (lastAddedIndex + 1);
        storage[lastAddedIndex] = item;
        return true;
    }

    public synchronized T poll() {
        if (lastAddedIndex == lastPolledIndex) {
            return null;
        }

        lastPolledIndex = lastPolledIndex == storage.length - 1 ? 0 : (lastPolledIndex + 1);

        return storage[lastPolledIndex];
    }

}
