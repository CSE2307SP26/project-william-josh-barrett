package main;

public class StartMenu {
    
    protected boolean exit;
    protected boolean switch_account;

    protected BankManager bank;
    protected IOUtils io;

    protected static final boolean EXIT = false;
    protected static final boolean SWITCH = true;

    private static enum startSelections {
        MIN, CREATE, EXIT, MAX
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
        System.out.println("2. Exit the app");
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

    public void doSelection(int selection) {
        switch (startSelections.values()[selection]) {
            case startSelections.CREATE: createAccountUI(); break;
            case startSelections.EXIT: exit = true; break;
            default: assert (false);
        }
    }

    public boolean menu() {
        while (true) {
            displayOptions();
            int selection = io.getUserSelection(startSelections.MAX.ordinal());
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
