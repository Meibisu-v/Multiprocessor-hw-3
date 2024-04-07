package ex_86;

import java.util.concurrent.locks.Lock;

public class Barrier1 {
    private int counter = 0;
    private final Lock lock = new TTASLock();

    public void waitAtBarrier(int n) {
        lock.lock();
        counter++;
        lock.unlock();
        while (counter < n) {
        }
    }
}
