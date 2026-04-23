package main;

public class MainMenu {

    private static final boolean EXIT = false;

    private BankManager bank = new BankManager();
    private IOUtils io = new IOUtils(bank);
    
    private BrokerMenu broker = new BrokerMenu(io, bank);
    private StartMenu start = new StartMenu(io, bank);
    private CustomerMenu customer = new CustomerMenu(io, bank, broker);
    private AdminMenu admin = new AdminMenu(io, bank, broker);

    public void run() {
        while (true) {
            if (doSelectedAction() == EXIT) {
                return;
            }
        }
    }

    public boolean doSelectedAction() {
        if (!bank.isLoggedIn()) {
            return start.menu();
        }
        if (!bank.isAdminLoggedIn()) {
            return customer.menu();
        } else {
            return admin.menu();
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}
