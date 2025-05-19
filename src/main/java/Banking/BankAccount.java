package Banking;

import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance;
    private final String accountId;
    private final ReentrantLock lock;

    public BankAccount(String accountId, double initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
        this.lock = new ReentrantLock();
    }

    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) {
        lock.lock();
        try {
            balance -= amount;
        } finally {
            lock.unlock();
        }
    }

    public void transfer(BankAccount target, double amount) {
        BankAccount firstLock = this.accountId.compareTo(target.accountId) < 0 ? this : target;
        BankAccount secondLock = this.accountId.compareTo(target.accountId) < 0 ? target : this;

        firstLock.lock.lock();
        secondLock.lock.lock();
        try {
            this.balance -= amount;
            target.balance += amount;
        } finally {
            secondLock.lock.unlock();
            firstLock.lock.unlock();
        }
    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public String getAccountId() {
        return accountId;
    }
}