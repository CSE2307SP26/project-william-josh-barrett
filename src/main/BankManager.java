package main;

import java.util.ArrayList;

public class BankManager {

    private static final int EMPTY = 0;
    private static final int ADMIN_ACCOUNT_INDEX = 0;
    private static final String ADMIN_ACCOUNT_NAME = "admin";

    private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
    private BankAccount curAccount;
    private FraudDetector fraudDetector;

    public BankManager() {
        accounts.add(new BankAccount(ADMIN_ACCOUNT_NAME));
        curAccount = null;
        fraudDetector = new FraudDetector();
    }

    public String getCurAccountName() {
        return curAccount.getName();
    }

    public double getBalance() {
        return curAccount.getBalance();
    }

    public boolean isLocked() {
        return curAccount.isLocked();
    }

    public void setPassword(String password) {
        curAccount.setPassword(password);
    }

    public boolean checkPassword(String password) {
        String curPassword = curAccount.getPassword();
        if (curPassword == null || curPassword.equals("")) {
            return password == null || password.equals("");
        }
        return curPassword.equals(password);
    }

    public boolean checkPassword(int index, String password) {
        String accountPassword = accounts.get(index).getPassword();
        if (accountPassword == null || accountPassword.equals("")) {
            return password == null || password.equals("");
        }
        return accountPassword.equals(password);
    }

    public int getSize() {
        return accounts.size();
    }

    public ArrayList<Security> getPortfolio() {
        return curAccount.getPortfolio();
    }

    public double getAccountBalance(int index) {
        return accounts.get(index).getBalance();
    }

    public ArrayList<String> getTransactionHistory() {
        return curAccount.getTransactionHistory();
    }

    public ArrayList<String> filterTransactionHistory(String transactionType) {
        ArrayList<String> matches = new ArrayList<String>();
        ArrayList<String> history = getTransactionHistory();

        if (transactionType == null) {
            return matches;
        }

        String loweredType = transactionType.toLowerCase();

        for (String entry : history) {
            String loweredEntry = entry.toLowerCase();

            if (loweredType.equals("deposit")) {
                if (loweredEntry.contains("deposit")) {
                    matches.add(entry);
                }
            } else if (loweredType.equals("withdrawal")) {
                if (loweredEntry.contains("withdrew") || loweredEntry.contains("withdrawal")) {
                    matches.add(entry);
                }
            } else if (loweredType.equals("transfer")) {
                if (loweredEntry.contains("transfer")) {
                    matches.add(entry);
                }
            }
        }

        return matches;
    }

    public void printTransactionEntries(ArrayList<String> entries) {
        if (entries.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (String entry : entries) {
            System.out.println(entry);
        }
    }

    public void addTransaction(String message) {
        curAccount.addTransaction(message);
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

    public boolean withdraw(double amount) {
        if (curAccount == null || curAccount.isLocked()) {
            return false;
        }

        try {
            curAccount.withdraw(amount);
            curAccount.addTransaction("Withdrawal: $" + amount);

            String fraudMessage = fraudDetector.detectFraud("withdraw", amount);
            handleSuspiciousActivity(curAccount, fraudMessage);

            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
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

    public int findAccountIndexByName(String name) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
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
        if (fromAccount == null || toAccount == null || fromAccount.isLocked()) {
            return false;
        }

        try {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);

            fromAccount.addTransaction("Transfer sent: $" + amount + " to " + toAccount.getName());
            toAccount.addTransaction("Transfer received: $" + amount + " from " + fromAccount.getName());

            String fraudMessage = fraudDetector.detectFraud("transfer", amount);
            handleSuspiciousActivity(fromAccount, fraudMessage);

            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public BankAccount createAccount(String name) {
        return createAccount(name, null, AccountType.CHECKING);
    }

    public BankAccount createAccount(String name, String password) {
        return createAccount(name, password, AccountType.CHECKING);
    }

    public BankAccount createAccount(String name, String password, AccountType type) {
        if (name.equalsIgnoreCase(ADMIN_ACCOUNT_NAME)) {
            curAccount = getAdminAccount();
            return curAccount;
        }

        BankAccount new_account;
        if (type == AccountType.SAVINGS) {
            new_account = new SavingsAccount(name);
        } else {
            new_account = new CheckingAccount(name);
        }

        new_account.setPassword(password);
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

    public boolean unlockAccount(int index) {
        if (isAdminAccount(index)) {
            return false;
        }
        accounts.get(index).unlockAccount();
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

    public void displayCustomerAccountsAndTypes() {
        ArrayList<BankAccount> customerAccounts = getCustomerAccounts();

        if (customerAccounts.isEmpty()) {
            System.out.println("No customer accounts found.");
            return;
        }

        System.out.println("Your accounts:");
        int index = 1;
        for (BankAccount account : customerAccounts) {
            System.out.println(index + ". " + account.getName() + " - " + account.getAccountType());
            index++;
        }
    }

    private void handleSuspiciousActivity(BankAccount account, String message) {
        if (message == null) {
            return;
        }

        account.addTransaction("FRAUD ALERT: " + message);
        account.incrementSuspiciousTransactionCount();

        if (account.getSuspiciousTransactionCount() >= 3) {
            account.lockAccount();
            account.addTransaction("ACCOUNT LOCKED: 3 suspicious activities detected.");
        }
    }

    public boolean currentAccountIsLocked() {
        return curAccount != null && curAccount.isLocked();
    }

    public boolean unlockCurrentAccount(String passwordAttempt) {
        if (curAccount == null) {
            return false;
        }

        if (checkPassword(passwordAttempt)) {
            curAccount.unlockAccount();
            curAccount.resetSuspiciousTransactionCount();
            curAccount.addTransaction("ACCOUNT UNLOCKED: Password verified.");
            return true;
        }

        return false;
    }

    /**
     * Set the interest rate for a SavingsAccount (admin only).
     * 
     * @param accountIndex the index of the account to modify
     * @param interestRate the new interest rate (must be between 0 and 1)
     * @return true if successful, false if account is not a SavingsAccount or index
     *         is invalid
     */
    public boolean setSavingsAccountInterestRate(int accountIndex, double interestRate) {
        if (accountIndex < 0 || accountIndex >= accounts.size()) {
            return false;
        }

        BankAccount account = accounts.get(accountIndex);
        if (!(account instanceof SavingsAccount)) {
            return false;
        }

        try {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.setInterestRate(interestRate);
            account.addTransaction("Interest rate updated to: " + (interestRate * 100) + "%");
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    /**
     * Set the daily transaction limit for a CheckingAccount (admin only).
     * 
     * @param accountIndex the index of the account to modify
     * @param dailyLimit   the new daily transaction limit
     * @return true if successful, false if account is not a CheckingAccount or
     *         index is invalid
     */
    public boolean setCheckingAccountDailyLimit(int accountIndex, double dailyLimit) {
        if (accountIndex < 0 || accountIndex >= accounts.size()) {
            return false;
        }

        BankAccount account = accounts.get(accountIndex);
        if (!(account instanceof CheckingAccount)) {
            return false;
        }

        try {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            checkingAccount.setDailyTransactionLimit(dailyLimit);
            account.addTransaction("Daily transaction limit updated to: $" + dailyLimit);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}


