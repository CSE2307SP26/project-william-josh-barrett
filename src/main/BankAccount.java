package main;

import java.util.ArrayList;

public class BankAccount {

    private double balance;
    private String accountName;
    private ArrayList<Security> portfolio;

    public BankAccount(String name) {
        this.balance = 0;
        this.accountName = name;
        this.portfolio = new ArrayList<Security>();
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
            this.balance = Math.round(balance*100.0)/100.0;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            this.balance = Math.round(balance*100.0)/100.0;
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
}
