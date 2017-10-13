package bastrich;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicIntegerArray;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author bastrich on 13.10.2017.
 */
public class ConcurrentFIFOTest {

    private ConcurrentFIFO<Integer> concurrentFIFO;
    private AtomicIntegerArray items = new AtomicIntegerArray(100);
    private volatile int[][] itemsByThreads;

    private final Object monitor1 = new Object();
    private final Object monitor2 = new Object();

    @BeforeTest
    public void init() {
        concurrentFIFO = new ConcurrentFIFO<>();
        for (int i = 1; i <= 100; i++) {
            items.set(i-1, i);
        }

        itemsByThreads = new int[10][10];
        for (int i = 0; i < itemsByThreads.length; i++) {
            for (int j = 0; j < itemsByThreads[i].length; j++) {
                itemsByThreads[i][j] = i * 10 + j + 1;
            }
        }
    }

    @Test
    public void singleThreadTest() throws Exception {
        for (int i = 1; i <= 105; i++) {
            concurrentFIFO.add(i);
        }

        for (int i = 1; i <= 105; i++) {
            assertEquals(new Integer(i), concurrentFIFO.poll());
        }
        assertEquals(null, concurrentFIFO.poll());
    }


    @Test(threadPoolSize = 10, invocationCount = 10, timeOut = 10000)
    public void multiThreadTest() throws Exception {

        synchronized (monitor1) {
            for (int i = 0; i < itemsByThreads.length; i++) {
                if (itemsByThreads[i] != null) {
                    for (int j = 0; j < itemsByThreads[i].length; j++) {
                        concurrentFIFO.add(itemsByThreads[i][j]);
                    }
                    itemsByThreads[i] = null;
                    itemsByThreads = itemsByThreads;
                    break;
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            int item = concurrentFIFO.poll();

            synchronized (monitor2) {
                boolean contains = false;
                int index = -1;
                for (int j = 0; j < 100; j++) {
                    if (items.get(j) == item) {
                        contains = true;
                        index = j;
                        break;
                    }
                }
                assertTrue(contains);

                items.set(index, -1);
            }
        }
    }

    @AfterClass
    public void after() {
        assertEquals(null, concurrentFIFO.poll());
        for (int i = 0; i < 100; i++) {
            assertEquals(-1, items.get(i));
        }
    }
}