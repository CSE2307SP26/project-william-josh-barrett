package main;

public class BankAccount {

    private double balance;
    private String accountName;

    public BankAccount(String name) {
        this.balance = 0;
        this.accountName = name;
    }

    public String getName() {
        return this.accountName;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public double getBalance() {
        return this.balance;
    }
}
