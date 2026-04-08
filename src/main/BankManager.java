package main;

import java.util.ArrayList;

public class BankManager {

    private static final int EMPTY = 0;
    private static final int ADMIN_ACCOUNT_INDEX = 0;
    private static final String ADMIN_ACCOUNT_NAME = "admin";

    private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
    private BankAccount curAccount;

    public BankManager() {
        accounts.add(new BankAccount(ADMIN_ACCOUNT_NAME));
        curAccount = null;
    }

    public String getCurAccountName() {
        return curAccount.getName();
    }

    public double getBalance() {
        return curAccount.getBalance();
    }

    public int getSize() {
        return accounts.size();
    }

    public double getAccountBalance(int index) {
        return accounts.get(index).getBalance();
    }

    public boolean checkBankIsEmpty() {
        for (BankAccount account : accounts) {
            if (account.getBalance() != EMPTY) {
                return false;
            }
        }
        return true;
    }

    public void deposit(double amount) {
        curAccount.deposit(amount);
    }

    public void withdraw(double amount) {
        curAccount.withdraw(amount);
    }
    
    public boolean isLoggedIn() {
        return curAccount != null;
    }

    public boolean isAdminAccount(int index) {
        return index == ADMIN_ACCOUNT_INDEX;
    }

    public boolean isAdminAccount(BankAccount account) {
        return account.getName().equalsIgnoreCase(ADMIN_ACCOUNT_NAME);
    }

    public boolean isAdminLoggedIn() {
        if (!isLoggedIn()) {
            return false;
        }
        return isAdminAccount(curAccount);
    }

    public BankAccount getAdminAccount() {
        for (BankAccount account : accounts) {
            if (isAdminAccount(account)) {
                return account;
            }
        }
        return null;
    }

    public void printAccounts() {
        int index = 1;
        for (BankAccount account : accounts) {
            System.out.println(index + ". " + account.getName());
            index++;
        }
    }

    public boolean transferIndex(int amount, int fromAccountIndex, int toAccountIndex) {
        return transferDirect(amount, accounts.get(fromAccountIndex), accounts.get(toAccountIndex));
    }

    public boolean transferDirect(int amount, BankAccount fromAccount, BankAccount toAccount) {
        try {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public BankAccount createAccount(String name) {
        if (name.equalsIgnoreCase(ADMIN_ACCOUNT_NAME)) {
            curAccount = getAdminAccount();
            return curAccount;
        }
        BankAccount new_account = new BankAccount(name);
        accounts.add(new_account);
        curAccount = accounts.get(accounts.size() - 1);
        return curAccount;
    }

    public boolean closeAccount(int index) {
        int curAccountIndex = accounts.indexOf(curAccount);
        accounts.remove(index);
        if (accounts.size() != EMPTY && index == curAccountIndex) {
            return false;
        }
        return true;
    }

    public void switchAccounts(int index) {
        curAccount = accounts.get(index);
    }

    public void addInterestPayment(int index, double amount) {
        accounts.get(index).addInterestPayment(amount);
    }

    public ArrayList<BankAccount> getCustomerAccounts() {
        ArrayList<BankAccount> customerAccounts = new ArrayList<BankAccount>();
        for (BankAccount account : accounts) {
            if (isAdminAccount(account)) {
                continue;
            }
            customerAccounts.add(account);
        }
        return customerAccounts;
    }

}
