import java.util.Random;
import java.util.ArrayList;
import java.util.List;

class Account extends Thread {
    int id;
    double balance;
    SavingsAccount myAccount;
    List<SavingsAccount> otherAccounts = new ArrayList<>();
    List<Double> values = new ArrayList<>();

    Account(double balance, int id) {
        this.id = id;
        this.balance = balance;
        this.myAccount = new SavingsAccount(this.balance);
    }

    String threadName() {
        return this.id == 0 ? "Boss" : "" + this.id;
    }

    public void run() {
        try {
            transfer();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addOtherAccount(SavingsAccount otherAccount, double value) {
        this.otherAccounts.add(otherAccount);
        this.values.add(value);
    }

    public void transfer() throws InterruptedException {
        for (int i = 0; i < otherAccounts.size(); i++) {
            this.myAccount.transfer(this.values.get(i), this.otherAccounts.get(i), true);
        }
    }

    public SavingsAccount savingsAccount() {
        return this.myAccount;
    }
}

public class SavingsAccountTest {
    public static void main(String[] args) throws Exception {
        int numberOfAccounts = 10;
        Random random = new Random();
        Account[] accounts = new Account[numberOfAccounts];
        accounts[0] = new Account((numberOfAccounts - 1) * 1000, 0);

        for (int i = 1; i < accounts.length; i++) {
            accounts[i] = new Account(1000, i);
        }

        for (int i = 1; i < accounts.length; i++) {
            int id = random.nextInt(accounts.length);
            accounts[i].addOtherAccount(accounts[id == 0 ? 1 : id].savingsAccount(), 100);
        }

        for (int i = 1; i < accounts.length; i++) {
            accounts[i].addOtherAccount(accounts[0].savingsAccount(), 1000);
        }

        for (Account account : accounts) {
            account.start();
        }

        for (Account account : accounts) {
            account.join();
        }

        System.out.println(accounts[0]);

        for (Account account : accounts) {
            System.out.println("[Thread " + account.threadName() + "] Final Balance: " + account.savingsAccount().getBalance());
        }
    }
}
