package main;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class MainMenu {

    private static final int EMPTY = 0;

    private static enum startSelections {
        MIN, CREATE, EXIT, MAX
    }

    private static enum accountSelections {
        MIN, DEPOSIT, TRANSFER, WITHDRAW, CHECK_BALANCE, BROKERAGE, SWITCH, CREATE, CLOSE, EXIT, MAX
    }

    private static enum adminSelections {
        MIN, DEPOSIT, TRANSFER, WITHDRAW, CHECK_BALANCE, BROKERAGE, SWITCH, CREATE, CLOSE, COLLECT_FEE, ADD_INTEREST, EXIT, MAX
    }

    private BankManager bank = new BankManager();
    private BrokerMenu broker;
    private Scanner keyboardInput;
    private boolean exit = false;

    public MainMenu() {
        this.keyboardInput = new Scanner(System.in);
        broker = new BrokerMenu(this, bank);
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

        if (!bank.isLoggedIn()) {
            System.out.println("You are not currently logged in.");
            System.out.println("1. Create an account");
            System.out.println("2. Exit the app");
        } else {
            System.out.println("Logged in as: " + bank.getCurAccountName());
            System.out.println("1. Make a deposit");
            System.out.println("2. Transfer a balance");
            System.out.println("3. Make a withdrawal");
            System.out.println("4. Check account balance");
            System.out.println("5. Open securities brokerage");
            System.out.println("6. Switch accounts");
            System.out.println("7. Create an account");
            System.out.println("8. Close an account");
            if (bank.isAdminLoggedIn()) {
                System.out.println("9. Collect a fee");
                System.out.println("10. Add an interest payment");
                System.out.println("11. Exit the app");
            } else {
                System.out.println("9. Exit the app");
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

    public int selectAccount(String message) {
        bank.printAccounts();
        while (true) {
            System.out.print(message);
            int accountIndex = scanInt() - 1;
            if (accountIndex < 0 || accountIndex >= bank.getSize()) {
                continue;
            }
            return accountIndex;
        }
    }

    public void depositUI() {
        int depositAmount = -1;
        while (depositAmount <= 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = scanInt();
            if (depositAmount > 0) {
                break;
            }
            System.out.println("A deposit larger than 0 is required. Please try again.");
        }
        bank.deposit(depositAmount);
    }

    public void withdrawalUI() {
        int withdrawalAmount = -1;
        while (withdrawalAmount <= 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawalAmount = scanInt();
            if (withdrawalAmount > 0) {
                break;
            }
            System.out.println("A withdrawal larger than 0 is required. Please try again.");
        }
        try {
            bank.withdraw(withdrawalAmount);
            System.out.println("Successfully withdrew $" + withdrawalAmount);
        } catch (IllegalArgumentException e) {
            System.out.println("Withdrawal amount exceeds account balance. Please try again.");
        }
    }

    public void getBalanceUI(){
        double balance = bank.getBalance();
        System.out.println("Current balance: $" + balance);
    }

    public void transferUI() {
        if (bank.checkBankIsEmpty()) {
            System.out.println("At least one of your accounts must have funds to transfer.");
            return;
        }

        int fromAccountIndex;
        while (true) {
            fromAccountIndex = selectAccount("Select an account to transfer from: ");
            if (bank.getAccountBalance(fromAccountIndex) > 0) {
                break;
            }
            System.out.println("This account has no money. Please try again.");
        }
        int toAccountIndex = selectAccount("Select an account to transfer to: ");
        while (true) {
            System.out.print("Enter the amount of money to be transferred: ");
            int amount = scanInt();
            if (bank.transferIndex(amount, fromAccountIndex, toAccountIndex)) {
                break;
            } else {
                System.out.println("Amount out of bounds. Please try again.");
            }
        }
    }

    public void createAccountUI() {
        System.out.print("Please enter the account name: ");
        String name = keyboardInput.nextLine();
        bank.createAccount(name);
        if (bank.isAdminLoggedIn()) {
            System.out.println("Logged in as admin.");
        }
    }

    public void closeAccountUI() {
        int selectedIndex = selectAccount("Please select an account to close: ");
        if (bank.isAdminAccount(selectedIndex)) {
            System.out.println("The admin account cannot be closed.");
            return;
        }
        if (!bank.closeAccount(selectedIndex)) {
            switchAccountsUI();
        }
    }

    public void collectFeeUI() {
        BankAccount validAccount = getCollectableCustomerAccount();
        if (validAccount == null) {
            return;
        }
        while (true) {
            System.out.print("Enter fee amount to collect: ");
            double amount = scanDouble();
            try {
                validAccount.withdraw(amount);
                System.out.println("Fee of $" + amount + " successfully collected.");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Fee amount out of bounds. Please try again.");
            }
        }
    }

    public BankAccount getCollectableCustomerAccount() {
        if (bank.getSize() <= 1) {
            System.out.println("No customer accounts available for fee collection.");
            return null;
        }
        if (bank.checkBankIsEmpty()) {
            System.out.println("No customer accounts have collectable funds.");
            return null;
        }
        ArrayList<BankAccount> customerAccounts;
        BankAccount selectedAccount;
        while (true) {
            customerAccounts = bank.getCustomerAccounts();
            selectedAccount = selectCustomerAccount(customerAccounts);
            if (selectedAccount.getBalance() == EMPTY) {
                System.out.println("This account has no funds. Please try again.");
                continue;
            }
            break;
        }
        return selectedAccount;
    }

    public BankAccount selectCustomerAccount(ArrayList<BankAccount> customerAccounts) {
        int displayIndex = 1;
        for (BankAccount account : customerAccounts) {
            System.out.println(displayIndex + ". " + account.getName());
            displayIndex++;
        }
        while (true) {
            System.out.print("Select an account: ");
            int selectedCustomerIndex = scanInt() - 1;
            if (selectedCustomerIndex >= 0 && selectedCustomerIndex < customerAccounts.size()) {
                return customerAccounts.get(selectedCustomerIndex);
            }
        }
    }

    public void addInterestPaymentUI() {
        if (bank.getSize() <= 1) {
            System.out.println("No customer accounts available for interest payments.");
            return;
        }

        ArrayList<BankAccount> customerAccountIndexes = bank.getCustomerAccounts();
        BankAccount selectedAccount = selectCustomerAccount(customerAccountIndexes);
        
        while (true) {
            System.out.print("Enter interest payment amount: ");
            double amount = scanDouble();
            try {
                selectedAccount.addInterestPayment(amount);
                System.out.println("Interest payment of $" + amount + " successfully added.");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Interest payment amount out of bounds. Please try again.");
            }
        }
    }

    public void switchAccountsUI() {
        bank.switchAccounts(selectAccount("Please select an account to switch to: "));
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
        if (!bank.isLoggedIn()) {
            selection = getUserSelection(startSelections.MAX.ordinal());
            doStartSelection(selection);
        } else {
            if (bank.isAdminLoggedIn()) {
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
            depositUI();
        } else if (selection == 2) {
            transferUI();
        } else if (selection == 3) {
            withdrawalUI();
        } else if (selection == 4){
            getBalanceUI();
        } else if (selection == 5){
            broker.open();
        } else if (selection == 6) {
            switchAccountsUI();
        } else if (selection == 7) {
            createAccountUI();
        } else if (selection == 8) {
            closeAccountUI();
        } else if (selection == 9) {
            collectFeeUI();
        } else if (selection == 10) {
            addInterestPaymentUI();
        } else if (selection == 11) {
            exit = true;
        } else {
            assert (false);
        }
    }

    public void doStartSelection(int selection) {
        if (selection == 1) {
            createAccountUI();
        } else if (selection == 2) {
            exit = true;
        } else {
            assert (false);
        }
    }

    public void doAccountSelection(int selection) {
        if (selection == 1) {
            depositUI();
        } else if (selection == 2) {
            transferUI();
        } else if (selection == 3) {
            withdrawalUI();
        } else if (selection == 4) {
            getBalanceUI();
        } else if (selection == 5){
            broker.open();
        } else if (selection == 6) {
            switchAccountsUI();
        } else if (selection == 7) {
            createAccountUI();
        } else if (selection == 8) {
            closeAccountUI();
        } else if (selection == 9) {
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
