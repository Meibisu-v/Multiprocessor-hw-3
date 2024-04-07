package ex_93;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class SimpleReadWriteLock implements ReadWriteLock {
    int readers;
    boolean writer;
    Object lock;
    Lock readLock, writeLock;

    public SimpleReadWriteLock() {
        writer = false;
        readers = 0;
        lock = new Object();
        readLock = new ReadLock();
        writeLock = new WriteLock();
    }
    public Lock readLock() {
        return readLock;
    }

    public Lock writeLock() {
        return writeLock;
    }
    private class ReadLock implements Lock {
        @Override
        public void lock() {
            synchronized (lock) {
                try {
                    while (writer) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            readers++;
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {
            synchronized (lock) {
                readers--;
                if (readers == 0) {
                    lock.notifyAll();
                }
            }
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }


    protected class WriteLock implements Lock {
        @Override
        public void lock() {
            synchronized (lock) {
                try {
                    while (readers > 0 || writer) {
                        lock.wait();
                    }
                    writer = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {
            synchronized (lock) {
                writer = false;
                lock.notifyAll();
            }
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }
}