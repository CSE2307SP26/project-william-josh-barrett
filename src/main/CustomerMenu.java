package main;

import java.util.ArrayList;

public class CustomerMenu extends StartMenu {

    protected BrokerMenu broker;
    
    private static enum customerSelections {
        MIN, DEPOSIT, TRANSFER, WITHDRAW, CHECK_BALANCE, HISTORY, BROKERAGE, SWITCH, CREATE, CLOSE, SET_PASSWORD, EXIT,
        MAX
    }

    CustomerMenu(IOUtils io, BankManager bank, BrokerMenu broker) {
        super(io, bank);
        this.broker = broker;
        this.exit = false;
        this.switch_account = false;
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("Logged in as: " + bank.getCurAccountName());
        System.out.println("1. Make a deposit");
        System.out.println("2. Transfer a balance");
        System.out.println("3. Make a withdrawal");
        System.out.println("4. Check account balance");
        System.out.println("5. Check transaction history");
        System.out.println("6. Open securities brokerage");
        System.out.println("7. Switch accounts");
        System.out.println("8. Create an account");
        System.out.println("9. Close an account");
        System.out.println("10. Set account password/pin");
        System.out.println("11. Exit the app");
    }

    protected boolean handleLockedAccount() {
        if (!bank.isLoggedIn()) {
            return false;
        }

        if (bank.isLocked()) {
            System.out.println("Account is locked due to suspicious activity.");
            System.out.print("Enter password to unlock: ");

            String password = io.scanLine();

            if (bank.unlockCurrentAccount(password)) {
                System.out.println("Account unlocked.");
                return true;
            } else {
                System.out.println("Incorrect password. Access denied.");
                return false;
            }
        }

        return true;
    }

    public void depositUI() {
        int depositAmount = -1;
        while (depositAmount <= 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = io.scanInt();
            if (depositAmount > 0) {
                break;
            }
            System.out.println("A deposit larger than 0 is required. Please try again.");
        }
        bank.deposit(depositAmount);
    }

    public void transferUI() {
        if (bank.checkBankIsEmpty()) {
            System.out.println("At least one of your accounts must have funds to transfer.");
            return;
        }

        int fromAccountIndex;
        while (true) {
            fromAccountIndex = io.selectAccount("Select an account to transfer from: ");
            if (bank.getAccountBalance(fromAccountIndex) > 0) {
                break;
            }
            System.out.println("This account has no money. Please try again.");
        }
        int toAccountIndex = io.selectAccount("Select an account to transfer to: ");
        while (true) {
            System.out.print("Enter the amount of money to be transferred: ");
            int amount = io.scanInt();
            if (bank.transferIndex(amount, fromAccountIndex, toAccountIndex)) {
                break;
            } else {
                System.out.println("Amount out of bounds. Please try again.");
            }
        }
    }

    public void withdrawalUI() {
        int withdrawalAmount = -1;
        while (withdrawalAmount <= 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawalAmount = io.scanInt();
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

    public void getBalanceUI() {
        double balance = bank.getBalance();
        System.out.println("Current balance: $" + balance);
    }

    public void displayTransactionHistory() {
        ArrayList<String> history = bank.getTransactionHistory();

        System.out.println("\nTransaction History:");
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (String entry : history) {
            System.out.println(entry);
        }
    }

    public void switchAccountsUI() {
        int index = io.selectAccount("Please select an account to switch to: ");
        System.out.print("Enter password/pin: ");
        String password = io.scanLine();

        if (bank.checkPassword(index, password)) {
            bank.switchAccounts(index);
            switch_account = true;
        } else {
            System.out.println("Incorrect password.");
        }
    }

    public void closeAccountUI() {
        int selectedIndex = io.selectAccount("Please select an account to close: ");
        if (bank.isAdminAccount(selectedIndex)) {
            System.out.println("The admin account cannot be closed.");
            return;
        }
        if (!bank.closeAccount(selectedIndex)) {
            switchAccountsUI();
        }
    }

    public void setPasswordUI() {
        System.out.print("Enter a new password/pin: ");
        String password = io.scanLine();
        bank.setPassword(password);
        System.out.println("Password successfully updated.");
    }

    public void doSelection(int selection) {
        if (!handleLockedAccount()) {
            return;
        }
        switch(customerSelections.values()[selection]) {
            case customerSelections.DEPOSIT: depositUI(); break;
            case customerSelections.TRANSFER: transferUI(); break;
            case customerSelections.WITHDRAW: withdrawalUI(); break;
            case customerSelections.CHECK_BALANCE: getBalanceUI(); break;
            case customerSelections.HISTORY: displayTransactionHistory(); break;
            case customerSelections.BROKERAGE: broker.open(); break;
            case customerSelections.SWITCH: switchAccountsUI(); break;
            case customerSelections.CREATE: createAccountUI(); break;
            case customerSelections.CLOSE: closeAccountUI(); break;
            case customerSelections.SET_PASSWORD: setPasswordUI(); break;
            case customerSelections.EXIT: exit = true; break;
            default: assert (false);
        }
    }

    public boolean menu() {
        while (true) {
            displayOptions();
            int selection = io.getUserSelection(customerSelections.MAX.ordinal());
            doSelection(selection);
            if (exit) {
                exit = false;
                return EXIT;
            }
            if (switch_account) {
                switch_account = false;
                return SWITCH;
            }
        }
    }
}
