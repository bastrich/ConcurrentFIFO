package bastrich;


import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author bastrich on 13.10.2017.
 */
public class ConcurrentFIFOTest {

    private ConcurrentFIFO<Integer> concurrentFIFO;


    @BeforeTest
    public void init() {
        concurrentFIFO = new ConcurrentFIFO<Integer>();
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


    @Test(threadPoolSize = 10, invocationCount = 50, timeOut = 10000)
    public void multiThreadTest() throws Exception {
        for (int i = 1; i <= 105; i++) {
            concurrentFIFO.add(i);
        }

        for (int i = 1; i <= 105; i++) {
            assertEquals(new Integer(i), concurrentFIFO.poll());
        }
        assertEquals(null, concurrentFIFO.poll());
    }

}