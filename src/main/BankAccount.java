package main;

import java.util.ArrayList;

public class BankAccount {

    private double amountOwed;
    private double balance;
    private String accountName;
    private String password;
    private ArrayList<Security> portfolio;
    private ArrayList<String> transactionHistory;
    private boolean locked;
    private int suspiciousTransactionCount;

    public BankAccount(String name) {
        this.amountOwed = 0;
        this.balance = 0;
        this.accountName = name;
        this.password = null;
        this.portfolio = new ArrayList<Security>();
        this.transactionHistory = new ArrayList<String>();
        this.transactionHistory.add("Account created.");
        this.locked = false;
        this.suspiciousTransactionCount = 0;
    }

    public double getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(double value) {
        amountOwed = value;
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

    public boolean isLocked() {
        return locked;
    }

    public void lockAccount() {
        locked = true;
    }

    public void unlockAccount() {
        locked = false;
        suspiciousTransactionCount = 0;
    }

    public int getSuspiciousTransactionCount() {
        return suspiciousTransactionCount;
    }

    public void incrementSuspiciousTransactionCount() {
        suspiciousTransactionCount++;
    }

    public void resetSuspiciousTransactionCount() {
        suspiciousTransactionCount = 0;
    }

    public ArrayList<Security> getPortfolio() {
        return portfolio;
    }

    public String getAccountType() {
        return "Bank Account";
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
