package com.example.javaconcurency;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestLinkedQueue {

    private static final int ITERATIONS = 10;
    private LinkedQueue<Integer> linkedQueue = new LinkedQueue<>();

    public TestLinkedQueue() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Test of isEmpty method of class LinkedQueue on empty Q.
     */
    @Test
    public void testIsEmptyTrue() {
        System.out.println("isEmpty");
        assertTrue(linkedQueue.isEmpty());
    }

    /**
     * Test of isEmpty method of class LinkedQueue on non-empty Q.
     */
    @Test
    public void testIsEmptyFalse() {
        System.out.println("isEmpty");
        linkedQueue.put(3);
        assertFalse(linkedQueue.isEmpty());
    }

    /**
     * Test of remove method of class LinkedQueue.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        linkedQueue.put(1);
        linkedQueue.put(2);
        linkedQueue.put(3);
        linkedQueue.size();
        linkedQueue.pop();
        assertFalse(linkedQueue.isEmpty());
    }

    /**
     * Test of remove method of class LinkedQueue on empty.
     */
    @Test
    public void testRemoveOnEmpty() {
        System.out.println("remove");
        linkedQueue.put(1);
        linkedQueue.pop();
        assertTrue(linkedQueue.isEmpty());
    }

    @Test
    public void testPerformanceOfLinkedQueue() throws InterruptedException {

        for (int numThreads = 1; numThreads < 11; numThreads++) {
            long startMillis = System.currentTimeMillis();
            ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
            for (int i = 0; i < numThreads; i++) {
                threadPool.submit(new Runnable() {
                    public void run() {
                        for (long i = 0; i < ITERATIONS; i++) {
                            int nextInt = ThreadLocalRandom.current().nextInt(100);
                            linkedQueue.put(nextInt);
                            linkedQueue.pop();
                        }
                    }
                });
            }
            threadPool.shutdown();
            threadPool.awaitTermination(5, TimeUnit.MINUTES);
            long totalMillis = System.currentTimeMillis() - startMillis;
            double throughput = (double) (numThreads * ITERATIONS * 2) / (double) totalMillis;
            System.out.println(String.format("%s with %d threads: %dms (throughput: %.1f ops/s)", LinkedQueue.class.getSimpleName(), numThreads,
                    totalMillis, throughput));
        }
    }
}