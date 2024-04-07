package ex_86;


import static java.lang.Thread.sleep;

public class Barrier {
    public static final int NUM_THREADS = 15;
    // Simulate foo
    static int foo = 0;
    static int bar = 0;

    public static void foo() {
        foo++;
    }

    // Simulate bar
    public static void bar() {
        bar--;

    }
    public static void main(String[] args) throws InterruptedException {


        Barrier2 barrier2 = new Barrier2(NUM_THREADS);

        long startTime = System.currentTimeMillis();

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                foo();
                barrier2.waitAtBarrier(threadId);
                bar();
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken for Barrier2: " + (endTime - startTime) + " ms");

        Barrier1 barrier = new Barrier1();

        startTime = System.currentTimeMillis();

        Thread[] threads_1 = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads_1[i] = new Thread(() -> {
                    foo();
                    barrier.waitAtBarrier(NUM_THREADS);
                    bar();
            });
            threads_1[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads_1) {
            thread.join(); // Wait for each thread to finish
        }

        endTime = System.currentTimeMillis();
        System.out.println("Time taken for Barrier1: " + (endTime - startTime) + " ms");


    }
}