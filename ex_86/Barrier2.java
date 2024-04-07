package ex_86;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.Lock;

public class Barrier2 {

    private int n;
    private static int[] b;

    public Barrier2(int numThreads) {
        n = numThreads;
        b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = 0;
        }
    }

    public void waitAtBarrier(int threadId) {
//        System.out.println("thread " + threadId);
        if (threadId == 0) {
            b[0] = 1;
            while (b[n - 1] != 2) {
            }

            b[threadId] = 2;
        } else if (threadId < n - 1) {
            while (b[threadId - 1] != 1) {
            }
            b[threadId] = 1;
            while (b[threadId + 1] != 2) {
            }
            b[threadId] = 2;
        } else if (threadId == n - 1) {
            while (b[n - 2] != 1) {
            }
            b[n - 1] = 2;
        }
//        System.out.println("thread " + threadId + "exit");
    }
}
