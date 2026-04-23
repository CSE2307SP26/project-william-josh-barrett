package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BrokerMenu {

    private static enum brokerSelections {
        MIN, BUY, SHORT_SELL, SELL, DISPLAY, LOAN, EXIT, MAX
    }

    private ArrayList<Security> curPortfolio;
    private Scanner keyboardInput;
    private BankManager bank;
    private IOUtils io;
    private LoanManager loanMenu;
    private boolean exit;
    private Random rng;

    public BrokerMenu(IOUtils io, BankManager bank) {
        this.keyboardInput = new Scanner(System.in);
        this.rng = new Random();
        this.bank = bank;
        this.io = io;
        this.loanMenu = new LoanManager(io, bank);
    }

    public void updatePortfolio() {
        curPortfolio = bank.getPortfolio();
    }

    public Security getSecurity(String name) {
        for (Security security : curPortfolio) {
            if (name.equals(security.getName())) {
                return security;
            }
        }
        return null;
    }

    public boolean checkSecurityOwnership(String name) {
        Security security = getSecurity(name);
        return security != null;
    }

    public boolean isShorted(String name) {
        return name.contains("(shorted)");
    }

    public void open() {
        exit = false;
        updatePortfolio();
        while (true) {
            displayMenu();
            int selection = io.getUserSelection(brokerSelections.MAX.ordinal());
            doSelectedAction(selection);
            if (exit) {
                break;
            }
        }
    }

    public void displayMenu() {
        System.out.println("Welcome to the 237 Bank Brokerage!");
        System.out.println("Logged in as: " + bank.getCurAccountName());
        System.out.println("1. Buy a security");
        System.out.println("2. Open a short position");
        System.out.println("3. Sell a security");
        System.out.println("4. View your portfolio");
        System.out.println("5. Take out a loan");
        System.out.println("6. Exit the brokerage");
    }

    public void doSelectedAction(int selection) {
        switch (brokerSelections.values()[selection]) {
            case BUY:
                attemptBuySecurity();
                break;
            case SHORT_SELL:
                attemptShort();
                break;
            case SELL:
                attemptSellSecurity();
                break;
            case DISPLAY:
                displayPortfolio();
                break;
            case LOAN:
                loanMenu.open();
                break;
            case EXIT:
                exit = true;
                break;
            default:
                assert (false);
        }
    }

    public void attemptBuySecurity() {
        System.out.print("Please enter the name of the security you want to purchase: ");
        String secName = keyboardInput.nextLine();
        if (checkSecurityOwnership(secName)) {
            buyMoreSecurity(secName);
            return;
        }
        buyNewSecurity(secName);
    }

    public void buyMoreSecurity(String securityName) {
        Security security = getSecurity(securityName);
        double secValue = security.getValue();
        System.out.println("You already own " + security.getAmount() + " of " + security.getName());
        System.out.println("The current value is $" + secValue);
        System.out.print("Enter the amount you would like to purchase: ");
        int amount = io.scanInt();
        if (amount <= 0) {
            System.out.println("Purchase cancelled.");
            return;
        }
        double buyValue = Math.round((amount * secValue) * 100.0) / 100.0;
        if (buyValue <= bank.getBalance()) {
            bank.withdraw(buyValue);
            security.setAmount(amount + security.getAmount());
            bank.addTransaction(
                    "Bought " + amount + " of " + security.getName() +
                            " for $" + buyValue);
            System.out.println("Purchase successful.");
            return;
        }
        System.out.println("Insufficient funds to complete purchase.");
    }

    public void buyNewSecurity(String securityName) {
        double value = Math.round((1.0 + 99.0 * rng.nextDouble()) * 100.0) / 100.0;
        System.out.println("The current value of " + securityName + " is $" + value);
        System.out.print("Enter the amount you would like to purchase: ");
        int amount = io.scanInt();
        if (amount <= 0) {
            System.out.println("Purchase cancelled.");
            return;
        }
        double buyValue = Math.round((amount * value) * 100.0) / 100.0;
        boolean shorted = isShorted(securityName);
        if (!shorted && buyValue > bank.getBalance()) {
            System.out.println("Insufficient funds to complete purchase.");
            return;
        }
        if (shorted) {
            curPortfolio.add(new Short(securityName, amount, buyValue));
        } else {
            bank.withdraw(buyValue);
            curPortfolio.add(new Security(securityName, amount, value));
        }
        bank.addTransaction(
                "Bought " + amount + " of " + securityName +
                        " for $" + buyValue);
        System.out.println("Purchase successful.");
    }

    public void attemptShort() {
        System.out.print("Please enter the name of the security you want to short: ");
        String shortName = keyboardInput.nextLine();
        if (checkSecurityOwnership(shortName)) {
            System.out.println("Please close your short position on this security before opening a new one.");
            return;
        }
        buyNewSecurity(shortName + " (shorted)");
    }

    public void attemptSellSecurity() {
        Security selection = selectPortfolioSecurity();
        if (selection == null) {
            return;
        }

        int sellAmount = getSellAmount(selection);
        if (sellAmount == -1) {
            return;
        }

        sellSecurity(selection, sellAmount);
    }

    public Security selectPortfolioSecurity() {
        if (!displayPortfolio()) {
            return null;
        }
        int selectionIndex = io.getUserSelection(curPortfolio.size() + 1) - 1;
        Security selection = curPortfolio.get(selectionIndex);
        System.out.println("You have selected " + selection.getName());
        return selection;
    }

    public int getSellAmount(Security selection) {
        System.out.print("Please enter the number of securities you would like to sell: ");
        int sellAmount = io.scanInt();
        if (sellAmount <= 0) {
            System.out.println("Sale Cancelled");
            return -1;
        }
        if (sellAmount > selection.getAmount()) {
            System.out.println("Number too large!");
            return -1;
        }
        return sellAmount;
    }

    public void sellSecurity(Security selection, int sellAmount) {
        double sellValueRaw = sellAmount * selection.getValue();
        double sellValueRounded = Math.round(sellValueRaw * 100.0) / 100.0;

        if (sellValueRounded > 0) {
            bank.deposit(sellValueRounded);
        }
        else if (sellValueRounded < 0) {
            if (Math.abs(sellValueRounded) > bank.getBalance()) {
                System.out.println("You do not have enough funds to close your position!");
                return;
            }
            bank.withdraw(Math.abs(sellValueRounded));
        }

        selection.setAmount(selection.getAmount() - sellAmount);
        if (selection.getAmount() == 0) {
            curPortfolio.remove(selection);
        }
        bank.addTransaction(
                "Sold " + sellAmount + " of " + selection.getName() +
                        " for $" + sellValueRounded);
        System.out.println("Sale successful. Value of sale: $" + sellValueRounded);
    }

    public boolean displayPortfolio() {
        if (curPortfolio.isEmpty()) {
            System.out.println("Your portfolio is empty.");
            return false;
        }
        System.out.println("Your portfolio:");
        int index = 1;
        for (Security security : curPortfolio) {
            System.out.println(index + ". " + security.getName());
            System.out.println("    Amount: " + security.getAmount());
            System.out.println(security.getValueFormatted());
            index++;
        }
        return true;
    }
}
