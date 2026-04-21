package main;

import java.util.ArrayList;

public class AdminMenu extends CustomerMenu {

    private static final int EMPTY = 0;

    private static enum adminSelections {
        MIN, DEPOSIT, TRANSFER, WITHDRAW, CHECK_BALANCE, HISTORY, BROKERAGE, SWITCH, CREATE, CLOSE, SET_PASSWORD,
        COLLECT_FEE, ADD_INTEREST, UNLOCK_ACCOUNT, EXIT, MAX
    }

    public AdminMenu(IOUtils io, BankManager bank, BrokerMenu broker) {
        super(io, bank, broker);
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
        System.out.println("11. Collect a fee");
        System.out.println("12. Add an interest payment");
        System.out.println("13. Unlock an account");
        System.out.println("14. Exit the app");
    }

    public void unlockAccountUI() {
        int selectedIndex = io.selectAccount("Please select an account to unlock: ");
        if (bank.isAdminAccount(selectedIndex)) {
            System.out.println("The admin account cannot be locked/unlocked.");
            return;
        }
        if (bank.unlockAccount(selectedIndex)) {
            System.out.println("Successfully unlocked the account.");
        } else {
            System.out.println("Failed to unlock the account.");
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
            int selectedCustomerIndex = io.scanInt() - 1;
            if (selectedCustomerIndex >= 0 && selectedCustomerIndex < customerAccounts.size()) {
                return customerAccounts.get(selectedCustomerIndex);
            }
        }
    }

    public void collectFeeUI() {
        BankAccount validAccount = getCollectableCustomerAccount();
        if (validAccount == null) {
            return;
        }
        while (true) {
            System.out.print("Enter fee amount to collect: ");
            double amount = io.scanDouble();
            try {
                validAccount.withdraw(amount);
                System.out.println("Fee of $" + amount + " successfully collected.");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Fee amount out of bounds. Please try again.");
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
            double amount = io.scanDouble();
            try {
                selectedAccount.addInterestPayment(amount);
                System.out.println("Interest payment of $" + amount + " successfully added.");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Interest payment amount out of bounds. Please try again.");
            }
        }
    }

    public void doSelection(int selection) {
        if (!handleLockedAccount()) {
            return;
        }
        switch (adminSelections.values()[selection]) {
            case DEPOSIT:
                depositUI();
                break;
            case TRANSFER:
                transferUI();
                break;
            case WITHDRAW:
                withdrawalUI();
                break;
            case CHECK_BALANCE:
                getBalanceUI();
                break;
            case HISTORY:
                displayTransactionHistory();
                break;
            case BROKERAGE:
                broker.open();
                break;
            case SWITCH:
                switchAccountsUI();
                switch_account = true;
                break;
            case CREATE:
                createAccountUI();
                break;
            case CLOSE:
                closeAccountUI();
                break;
            case SET_PASSWORD:
                setPasswordUI();
                break;
            case COLLECT_FEE:
                collectFeeUI();
                break;
            case ADD_INTEREST:
                addInterestPaymentUI();
                break;
            case UNLOCK_ACCOUNT:
                unlockAccountUI();
                break;
            case EXIT:
                exit = true;
                break;
            default:
                assert (false);
        }
    }

    public boolean menu() {
        while (true) {
            displayOptions();
            int selection = io.getUserSelection(adminSelections.MAX.ordinal());
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
