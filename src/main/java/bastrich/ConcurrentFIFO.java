package bastrich;

/**
 * @author bastrich on 13.10.2017.
 */
public class ConcurrentFIFO<T> {

    private static int DEFAULT_CAPACITY = 10;
    private static int CAPACITY_STEP = 10;

    private T[] storage;
    private int last;

    public ConcurrentFIFO() {
        storage = (T[])new Object[DEFAULT_CAPACITY];
        last = -1;
    }

    public boolean add(T item) {
        if (last >= storage.length-1) {
            T[] newStorage = (T[])new Object[storage.length + CAPACITY_STEP];
            System.arraycopy(storage, 0, newStorage, 0, storage.length);
            newStorage[storage.length] = item;
            last = storage.length;
            storage = newStorage;
        } else {
            storage[last+1] = item;
            last++;
        }
        return true;
    }

    public T poll() {
        if (last == -1) {
            return null;
        }

        T result = storage[0];

        for (int i = 1; i <= last; i++) {
            storage[i-1] = storage[i];
        }
        last--;

        return result;
    }

}
