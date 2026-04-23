package main;

public class StartMenu {

    protected boolean exit;
    protected boolean switch_account;

    protected BankManager bank;
    protected IOUtils io;

    protected static final boolean EXIT = false;
    protected static final boolean SWITCH = true;

    private static enum startSelections {
        MIN, CREATE, LOGIN, EXIT, MAX
    }

    StartMenu(IOUtils io, BankManager bank) {
        this.io = io;
        this.bank = bank;
        this.exit = false;
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("You are not currently logged in.");
        System.out.println("1. Create an account");
        System.out.println("2. Login");
        System.out.println("3. Exit the app");
    }

    public void createAccountUI() {
        System.out.print("Please enter the account name: ");
        String name = io.scanLine();

        String typeStr = "";
        while (true) {
            System.out.print("What type of account? (checking/savings): ");
            typeStr = io.scanLine().trim();
            if (typeStr.equalsIgnoreCase("checking") || typeStr.equalsIgnoreCase("savings")) {
                break;
            }
            System.out.println("Invalid account type. Please try again.");
        }

        System.out.print("Please enter a password/pin (or leave blank for none): ");
        String password = io.scanLine();

        if (password.isEmpty()) {
            password = null;
        }

        AccountType type = typeStr.equalsIgnoreCase("savings") ? AccountType.SAVINGS : AccountType.CHECKING;
        bank.createAccount(name, password, type);
        if (bank.isAdminLoggedIn()) {
            System.out.println("Logged in as admin.");
        }

        switch_account = true;
    }

    public void loginUI() {
        if (bank.getSize() <= 1) {
            System.out.println("No accounts available. Please create an account first.");
            return;
        }

        System.out.println("Available accounts:");
        bank.printAccounts();

        System.out.print("Enter account name to login: ");
        String accountName = io.scanLine();

        int accountIndex = bank.findAccountIndexByName(accountName);
        if (accountIndex == -1) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter password/pin (or leave blank if none): ");
        String password = io.scanLine();

        if (bank.checkPassword(accountIndex, password)) {
            bank.switchAccounts(accountIndex);
            System.out.println("Successfully logged in as " + accountName + ".");
        } else {
            System.out.println("Incorrect password.");
        }
    }

    public void doSelection(int selection) {
        switch (startSelections.values()[selection]) {
            case CREATE:
                createAccountUI();
                break;
            case LOGIN:
                loginUI();
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
            int selection = io.getUserSelection(startSelections.MAX.ordinal());
            doSelection(selection);
            if (bank.isLoggedIn()) {
                return SWITCH;
            }
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
