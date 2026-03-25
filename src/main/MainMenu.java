package main;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class MainMenu {

    private static final int EMPTY = 0;
    private static enum startSelections {MIN, CREATE, EXIT, MAX}
    private static enum accountSelections {MIN, DEPOSIT, TRANSFER, SWITCH, CREATE, CLOSE, EXIT, MAX}

    private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
    private Scanner keyboardInput;
    private boolean exit = false;
    private int curAccountIndex;

    public MainMenu() {
        this.keyboardInput = new Scanner(System.in);
    }

    public int scanInt() {
        while (true) {
            try {
                int integer = keyboardInput.nextInt();
                keyboardInput.nextLine();
                return integer;
            } 
            catch (InputMismatchException e) {
                System.out.print("Invalid integer, please try again: ");
            }
            catch (NoSuchElementException e) {
                System.out.print("No input detected, please try again: ");
            }
            keyboardInput.nextLine();
        }
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        
        if (accounts.size() == EMPTY) {
            System.out.println("You are not currently logged in.");
            System.out.println("1. Create an account");
            System.out.println("2. Exit the app");
        } else {
            System.out.println("Logged in as: " + accounts.get(curAccountIndex).getName());
            System.out.println("1. Make a deposit");
            System.out.println("2. Transfer a balance");
            System.out.println("3. Switch accounts");
            System.out.println("4. Create an account");
            System.out.println("5. Close an account");
            System.out.println("6. Exit the app");
        }
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while(selection < 1 || selection >= max) {
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
            if (accountIndex < 0 || accountIndex >= accounts.size()) {continue;}
            return accountIndex;
        }
    }

    public void performDeposit() {
        double depositAmount = -1;
        while(depositAmount <= 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = scanInt();
            if (depositAmount > 0) {break;}
            System.out.println("A deposit larger than 0 is required. Please try again.");
        }
        accounts.get(curAccountIndex).deposit(depositAmount);
    }

    public void transferUI() {
        boolean hasMoney = false;
        for (BankAccount account : accounts) {
            if (account.getBalance() != EMPTY) {hasMoney = true;}
        }
        if (!hasMoney) {
            System.out.println("At least one of your accounts must have funds to transfer.");
            return;
        }
        
        int fromAccountIndex;
        while (true) {
            fromAccountIndex = selectAccount("Select an account to transfer from: ");
            if (accounts.get(fromAccountIndex).getBalance() > 0) {break;}
            System.out.println("This account has no money. Please try again.");
        }
        int toAccountIndex = selectAccount("Select an account to transfer to: ");
        while (true) {
            System.out.print("Enter the amount of money to be transferred: ");
            int amount = scanInt();
            if (transferDirect(amount, fromAccountIndex, toAccountIndex)) {break;}
        }
    }

    public boolean transferDirect(int amount, int fromAccountIndex, int toAccountIndex) {
        try {
            accounts.get(fromAccountIndex).withdraw(amount);
            accounts.get(toAccountIndex).deposit(amount);
            return true;
        }
        catch (IllegalArgumentException e) {
            System.out.println("Amount out of bounds. Please try again.");
            return false;
        }
    }

    public void createAccount() {
        System.out.print("Please enter the account name: ");
        String name = keyboardInput.nextLine();
        BankAccount new_account = new BankAccount(name);
        accounts.add(new_account);
        curAccountIndex = accounts.size() - 1;
    }

    public void closeAccount() {
        int selectedIndex = selectAccount("Please select an account to close: ");
        accounts.remove(selectedIndex);
        if (accounts.size() != EMPTY && selectedIndex == curAccountIndex) {
            switchAccounts();
        }
        if (selectedIndex < curAccountIndex) {curAccountIndex--;}
    }

    public void switchAccounts() {
        curAccountIndex = selectAccount("Please select an account to switch to: ");
    }

    public void run() {
        while (true) {
            doSelectedAction();
            if (exit) {return;}
        }
    }

    public void doSelectedAction() {
        int selection = -1;
        displayOptions();
        if (accounts.size() == EMPTY) {
            selection = getUserSelection(startSelections.MAX.ordinal());
            doStartSelection(selection);
        } else {
            selection = getUserSelection(accountSelections.MAX.ordinal());
            doAccountSelection(selection);
        }
    }

    public void doStartSelection(int selection) {
        switch (startSelections.values()[selection]) {
            case startSelections.CREATE: createAccount(); break;
            case startSelections.EXIT: exit = true; break;
            default: assert(false);
        }
    }

    public void doAccountSelection(int selection) {
        switch (accountSelections.values()[selection]) {
            case accountSelections.DEPOSIT: performDeposit(); break;
            case accountSelections.TRANSFER: transferUI(); break;
            case accountSelections.SWITCH: switchAccounts(); break;
            case accountSelections.CREATE: createAccount(); break;
            case accountSelections.CLOSE: closeAccount(); break;
            case accountSelections.EXIT: exit = true; break;
            default: assert(false);
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }

}
