package main;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class MainMenu {

    private static final int EMPTY = 0;
    private static enum startSelections {MIN, CREATE, EXIT, MAX}
    private static enum accountSelections {MIN, DEPOSIT, SWITCH, CREATE, EXIT, MAX}

    private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
    private Scanner keyboardInput;
    private boolean exit = false;
    private int curAccount;

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
                System.out.println("Invalid integer, please try again.");
            }
            catch (NoSuchElementException e) {
                System.out.println("No input detected, please try again.");
            }
            keyboardInput.nextLine();
        }
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        
        if (accounts.size() == EMPTY) {
            System.out.println("1. Create an account");
            System.out.println("2. Exit the app");
        } else {
            System.out.println("1. Make a deposit");
            System.out.println("2. Switch accounts");
            System.out.println("3. Create an account");
            System.out.println("4. Exit the app");
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

    public void createAccount() {
        System.out.print("Please enter the account name: ");
        String name = keyboardInput.nextLine();
        BankAccount new_account = new BankAccount(name);
        accounts.add(new_account);
        curAccount = accounts.size() - 1;
    }

    public void performDeposit() {
        double depositAmount = -1;
        while(depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = scanInt();
        }
        accounts.get(curAccount).deposit(depositAmount);
    }

    public void switchAccounts() {
        int index = 1;
        for (BankAccount account : accounts) {
            System.out.println(index + ". " + account.getName());
            index++;
        }
        while(true) {
            System.out.print("Please select an account: ");
            int accountIndex = scanInt() - 1;
            if (accountIndex < 0 || accountIndex >= accounts.size()) {continue;}
            curAccount = accountIndex;
            break;
        }
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
            case accountSelections.SWITCH: switchAccounts(); break;
            case accountSelections.CREATE: createAccount(); break;
            case accountSelections.EXIT: exit = true; break;
            default: assert(false);
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }

}
