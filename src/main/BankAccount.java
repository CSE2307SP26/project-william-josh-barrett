package main;

import java.util.ArrayList;

public class BankAccount {

    private double balance;
    private String accountName;
    private String password;
    private ArrayList<Security> portfolio;
    private ArrayList<String> transactionHistory;

    public BankAccount(String name) {
        this.balance = 0;
        this.accountName = name;
        this.password = null;
        this.portfolio = new ArrayList<Security>();
        this.transactionHistory = new ArrayList<String>();
        this.transactionHistory.add("Account created.");
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.accountName;
    }

    public ArrayList<Security> getPortfolio() {
        return portfolio;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            this.balance = Math.round(balance * 100.0) / 100.0;
            transactionHistory.add("Deposited $" + amount + ". New balance: $" + balance);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            this.balance = Math.round(balance * 100.0) / 100.0;
            transactionHistory.add("Withdrew $" + amount + ". New balance: $" + balance);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void addInterestPayment(double amount) {
        deposit(amount);
    }

    public double getBalance() {
        return this.balance;
    }

    public ArrayList<String> getTransactionHistory() {
        return new ArrayList<String>(transactionHistory);
    }

    public void addTransaction(String message) {
        this.transactionHistory.add(message);
    }
}
