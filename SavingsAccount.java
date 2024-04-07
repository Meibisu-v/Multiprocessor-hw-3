import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SavingsAccount{
    private double balance;
    int preferredWaiting ;
    private Lock lock = new ReentrantLock();
    private Lock lockForTransfer = new ReentrantLock();
    private final Condition condition = lock.newCondition();
     SavingsAccount(double initialBalance) {
        this.balance = initialBalance;
    }
    SavingsAccount() {
        this.balance = 0;
    }
    void withdraw(boolean preferred, double amount) throws InterruptedException {
        lock.lock();
        try {
            if ( preferred ) {
                preferredWaiting++;
                while (preferredWaiting > 1) {
                    System.out.println(this.toString() + " +1 in p queue");
                    condition.await();
                }
                preferredWaiting--;
                System.out.println(this.toString() + " -1 in p queue");
            } else {
                while (preferredWaiting > 0) {
                    System.out.println(this.toString() + " +1 in queue");
                    condition.await();
                }
                System.out.println(this.toString() + " -1 in  queue");
            }
            balance -= amount;
            condition.signalAll();
        } catch (InterruptedException ignored) {}
        finally {
            lock.unlock();
        }
    }
    void deposit(Double amount) {
        lock.lock();
        try {
            balance += amount;
            System.out.println(this.toString() + " increased by " + amount);
        } finally {
            lock.unlock();
        }
    }

    public double getBalance() {
        return balance;
    }

    void transfer(Double amount, SavingsAccount from, boolean preferred) throws InterruptedException {
        lockForTransfer.lock();
        try {
            System.out.println("from " + from.toString() + "to " + this.toString() + " amount " + amount);
            from.withdraw(preferred, amount);
            deposit(amount);
        } finally {
            lockForTransfer.unlock();
        }
    }
}

