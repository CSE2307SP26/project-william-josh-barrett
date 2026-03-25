package main;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class MainMenu {

    private static final int EMPTY = 0;
    private static final int LOGGED_OUT_INDEX = -1;
    private static final String ADMIN_ACCOUNT_NAME = "admin";

    private static enum startSelections {
        MIN, CREATE, EXIT, MAX
    }

    private static enum accountSelections {
        MIN, DEPOSIT, WITHDRAW TRANSFER, SWITCH, CREATE, CLOSE, EXIT, MAX
    }

    private static enum adminSelections {
        MIN, DEPOSIT, WITHDRAW, TRANSFER, SWITCH, CREATE, CLOSE, COLLECT_FEE, ADD_INTEREST, EXIT, MAX
    }

    private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
    private Scanner keyboardInput;
    private boolean exit = false;
    private int curAccountIndex;

    public MainMenu() {
        this.keyboardInput = new Scanner(System.in);
        accounts.add(new BankAccount(ADMIN_ACCOUNT_NAME));
        curAccountIndex = LOGGED_OUT_INDEX;
    }

    public boolean isLoggedIn() {
        return curAccountIndex != LOGGED_OUT_INDEX;
    }

    public boolean isAdminAccount(int accountIndex) {
        return accounts.get(accountIndex).getName().equalsIgnoreCase(ADMIN_ACCOUNT_NAME);
    }

    public boolean isAdminLoggedIn() {
        if (!isLoggedIn()) {
            return false;
        }
        return isAdminAccount(curAccountIndex);
    }

    public int getAdminAccountIndex() {
        for (int index = 0; index < accounts.size(); index++) {
            if (isAdminAccount(index)) {
                return index;
            }
        }
        return -1;
    }

    public int scanInt() {
        while (true) {
            try {
                int integer = keyboardInput.nextInt();
                keyboardInput.nextLine();
                return integer;
            } catch (InputMismatchException e) {
                System.out.print("Invalid integer, please try again: ");
            } catch (NoSuchElementException e) {
                System.out.print("No input detected, please try again: ");
            }
            keyboardInput.nextLine();
        }
    }

    public double scanDouble() {
        while (true) {
            try {
                double doubleValue = keyboardInput.nextDouble();
                keyboardInput.nextLine();
                return doubleValue;
            } catch (InputMismatchException e) {
                System.out.print("Invalid amount, please try again: ");
            } catch (NoSuchElementException e) {
                System.out.print("No input detected, please try again: ");
            }
            keyboardInput.nextLine();
        }
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");

        if (!isLoggedIn()) {
            System.out.println("You are not currently logged in.");
            System.out.println("1. Create an account");
            System.out.println("2. Exit the app");
        } else {
            System.out.println("Logged in as: " + accounts.get(curAccountIndex).getName());
            System.out.println("1. Make a deposit");
            System.out.println("2. Transfer a balance");
            System.out.println("3. Make a withdrawal");
            System.out.println("4. Switch accounts");
            System.out.println("5. Create an account");
            System.out.println("6. Close an account");
            if (isAdminLoggedIn()) {
                System.out.println("7. Collect a fee");
                System.out.println("8. Add an interest payment");
                System.out.println("9. Exit the app");
            } else {
                System.out.println("7. Exit the app");
            }
        }
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while (selection < 1 || selection >= max) {
            System.out.print("Please make a selection: ");
            selection = scanInt();
        }
        return selection;
    }

    public void printAccounts() {
        int index = 1;
        for (BankAccount account : accounts) {
            System.out.println(index + ". " + account.getName());
            index++;
        }
    }

    public int selectAccount(String message) {
        printAccounts();
        while (true) {
            System.out.print(message);
            int accountIndex = scanInt() - 1;
            if (accountIndex < 0 || accountIndex >= accounts.size()) {
                continue;
            }
            return accountIndex;
        }
    }

    public void performDeposit() {
        double depositAmount = -1;
        while (depositAmount <= 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = scanInt();
            if (depositAmount > 0) {
                break;
            }
            System.out.println("A deposit larger than 0 is required. Please try again.");
        }
        accounts.get(curAccountIndex).deposit(depositAmount);
    }

    public void performWithdrawal() {
        double withdrawalAmount = -1;
        while (withdrawalAmount <= 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawalAmount = scanInt();
            if (withdrawalAmount > 0) {
                break;
            }
            System.out.println("A withdrawal larger than 0 is required. Please try again.");
        }
        try {
            accounts.get(curAccountIndex).withdraw(withdrawalAmount);
            System.out.println("Successfully withdrew $" + withdrawalAmount);
            break;
        } catch (IllegalArgumentException e) {
            System.out.println("Withdrawal amount exceeds account balance. Please try again.");
        }
    }

    public void transferUI() {
        boolean hasMoney = false;
        for (BankAccount account : accounts) {
            if (account.getBalance() != EMPTY) {
                hasMoney = true;
            }
        }
        if (!hasMoney) {
            System.out.println("At least one of your accounts must have funds to transfer.");
            return;
        }

        int fromAccountIndex;
        while (true) {
            fromAccountIndex = selectAccount("Select an account to transfer from: ");
            if (accounts.get(fromAccountIndex).getBalance() > 0) {
                break;
            }
            System.out.println("This account has no money. Please try again.");
        }
        int toAccountIndex = selectAccount("Select an account to transfer to: ");
        while (true) {
            System.out.print("Enter the amount of money to be transferred: ");
            int amount = scanInt();
            if (transferDirect(amount, fromAccountIndex, toAccountIndex)) {
                break;
            }
        }
    }

    public boolean transferDirect(int amount, int fromAccountIndex, int toAccountIndex) {
        try {
            accounts.get(fromAccountIndex).withdraw(amount);
            accounts.get(toAccountIndex).deposit(amount);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Amount out of bounds. Please try again.");
            return false;
        }
    }

    public void createAccount() {
        System.out.print("Please enter the account name: ");
        String name = keyboardInput.nextLine();
        if (name.equalsIgnoreCase(ADMIN_ACCOUNT_NAME)) {
            int adminIndex = getAdminAccountIndex();
            curAccountIndex = adminIndex;
            System.out.println("Logged in as admin.");
            return;
        }
        BankAccount new_account = new BankAccount(name);
        accounts.add(new_account);
        curAccountIndex = accounts.size() - 1;
    }

    public void closeAccount() {
        int selectedIndex = selectAccount("Please select an account to close: ");
        if (isAdminAccount(selectedIndex)) {
            System.out.println("The admin account cannot be closed.");
            return;
        }
        accounts.remove(selectedIndex);
        if (accounts.size() != EMPTY && selectedIndex == curAccountIndex) {
            switchAccounts();
        }
        if (selectedIndex < curAccountIndex) {
            curAccountIndex--;
        }
    }

    public void collectFeeUI() {
        if (accounts.size() <= 1) {
            System.out.println("No customer accounts available for fee collection.");
            return;
        }

        ArrayList<Integer> customerAccountIndexes = getCustomerAccountIndexes();
        int accountIndex = selectCustomerAccount(customerAccountIndexes);
        collectFeeFromAccount(accountIndex);
    }

    public ArrayList<Integer> getCustomerAccountIndexes() {
        ArrayList<Integer> customerAccountIndexes = new ArrayList<Integer>();
        int displayIndex = 1;
        for (int accountIndex = 0; accountIndex < accounts.size(); accountIndex++) {
            if (isAdminAccount(accountIndex)) {
                continue;
            }
            System.out.println(displayIndex + ". " + accounts.get(accountIndex).getName());
            customerAccountIndexes.add(accountIndex);
            displayIndex++;
        }
        return customerAccountIndexes;
    }

    public int selectCustomerAccount(ArrayList<Integer> customerAccountIndexes) {
        int selectedCustomerIndex;
        while (true) {
            System.out.print("Select an account to collect a fee from: ");
            selectedCustomerIndex = scanInt() - 1;
            if (selectedCustomerIndex >= 0 && selectedCustomerIndex < customerAccountIndexes.size()) {
                break;
            }
        }
        return customerAccountIndexes.get(selectedCustomerIndex);
    }

    public void collectFeeFromAccount(int accountIndex) {
        while (true) {
            System.out.print("Enter fee amount to collect: ");
            double amount = scanDouble();
            try {
                accounts.get(accountIndex).withdraw(amount);
                System.out.println("Fee of $" + amount + " successfully collected.");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Fee amount out of bounds. Please try again.");
            }
        }
    }

    public void addInterestPaymentUI() {
        if (accounts.size() <= 1) {
            System.out.println("No customer accounts available for interest payments.");
            return;
        }

        ArrayList<Integer> customerAccountIndexes = getCustomerAccountIndexes();
        int accountIndex = selectCustomerAccount(customerAccountIndexes);
        addInterestPaymentToAccount(accountIndex);
    }

    public void addInterestPaymentToAccount(int accountIndex) {
        while (true) {
            System.out.print("Enter interest payment amount: ");
            double amount = scanDouble();
            try {
                accounts.get(accountIndex).addInterestPayment(amount);
                System.out.println("Interest payment of $" + amount + " successfully added.");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Interest payment amount out of bounds. Please try again.");
            }
        }
    }

    public void switchAccounts() {
        curAccountIndex = selectAccount("Please select an account to switch to: ");
    }

    public void run() {
        while (true) {
            doSelectedAction();
            if (exit) {
                return;
            }
        }
    }

    public void doSelectedAction() {
        int selection = -1;
        displayOptions();
        if (!isLoggedIn()) {
            selection = getUserSelection(startSelections.MAX.ordinal());
            doStartSelection(selection);
        } else {
            if (isAdminLoggedIn()) {
                selection = getUserSelection(adminSelections.MAX.ordinal());
                doAdminSelection(selection);
            } else {
                selection = getUserSelection(accountSelections.MAX.ordinal());
                doAccountSelection(selection);
            }
        }
    }

    public void doAdminSelection(int selection) {
        if (selection == 1) {
            performDeposit();
        } else if (selection == 2) {
            transferUI();
        } else if (selection == 3) {
            performWithdrawal();
        }else if (selection == 4) {
            switchAccounts();
        } else if (selection == 5) {
            createAccount();
        } else if (selection == 6) {
            closeAccount();
        } else if (selection == 7) {
            collectFeeUI();
        } else if (selection == 8) {
            addInterestPaymentUI();
        } else if (selection == 9) {
            exit = true;
        } else {
            assert (false);
        }
    }

    public void doStartSelection(int selection) {
        if (selection == 1) {
            createAccount();
        } else if (selection == 2) {
            exit = true;
        } else {
            assert (false);
        }
    }

    public void doAccountSelection(int selection) {
        if (selection == 1) {
            performDeposit();
        } else if (selection == 2) {
            transferUI();
        } else if (selection == 3) {
            performWithdrawal();
        } else if (selection == 4) {
            switchAccounts();
        } else if (selection == 5) {
            createAccount();
        } else if (selection == 6) {
            closeAccount();
        } else if (selection == 7) {
            exit = true;
        } else {
            assert (false);
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }

}
