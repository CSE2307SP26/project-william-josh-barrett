package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BrokerMenu {

    private static final int BROKER_OPTIONS_MAX = 5;

    private ArrayList<Security> curPortfolio;
    private Scanner keyboardInput;
    private BankManager bank;
    private MainMenu menu;
    private boolean exit;
    private Random rng;

    public BrokerMenu(MainMenu mainMenu, BankManager bankManager) {
        keyboardInput = new Scanner(System.in);
        rng = new Random();
        bank = bankManager;
        menu = mainMenu;
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

    public void open() {
        exit = false;
        updatePortfolio();
        while (true) {
            displayMenu();
            int selection = menu.getUserSelection(BROKER_OPTIONS_MAX);
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
        System.out.println("2. Sell a security");
        System.out.println("3. View your portfolio");
        System.out.println("4. Exit the brokerage");
    }

    public void doSelectedAction(int selection) {
        switch (selection) {
            case 1: 
                attemptBuySecurity();
                break;
            case 2:
                attemptSellSecurity();
                break;
            case 3: 
                displayPortfolio();
                break;
            case 4: 
                exit = true;
                break;
            default: assert(false);
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
        int amount = menu.scanInt();
        if (amount <= 0) {
            System.out.println("Purchase cancelled.");
            return;
        }
        double buyValue = Math.round((amount * secValue)*100.0)/100.0;
        if (buyValue <= bank.getBalance()) {
            bank.withdraw(buyValue);
            security.setAmount(amount + security.getAmount());
            bank.getCurrentAccount().addTransaction(
                "Bought " + amount + " of " + security.getName() +
                " for $" + String.format("%.2f", buyValue)
    );
            System.out.println("Purchase successful.");
            return;
        }
        System.out.println("Insufficient funds to complete purchase.");
    }

    public void buyNewSecurity(String securityName) {
        double value = Math.round((1.0 + 99.0*rng.nextDouble())*100.0)/100.0;
        System.out.println("The current value of " + securityName + " is $" + value);
        System.out.print("Enter the amount you would like to purchase: ");
        int amount = menu.scanInt();
        if (amount <= 0) {
            System.out.println("Purchase cancelled.");
            return;
        }
        double buyValue = Math.round((amount * value)*100.0)/100.0;
        if (buyValue <= bank.getBalance()) {
            bank.withdraw(buyValue);
            curPortfolio.add(new Security(securityName, amount, value));
            bank.getCurrentAccount().addTransaction(
                "Bought " + amount + " of " + securityName +
                " for $" + String.format("%.2f", buyValue)
    );
            System.out.println("Purchase successful.");
            return;
        }
        System.out.println("Insufficient funds to complete purchase.");
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
        int selectionIndex = menu.getUserSelection(curPortfolio.size() + 1) - 1;
        Security selection = curPortfolio.get(selectionIndex);
        System.out.println("You have selected " + selection.getName());
        return selection;
    }

    public int getSellAmount(Security selection) {
        System.out.print("Please enter the number of securities you would like to sell: ");
        int sellAmount = menu.scanInt();
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
        selection.setAmount(selection.getAmount() - sellAmount);
        if (selection.getAmount() == 0) {
            curPortfolio.remove(selection);
        }
        double sellValueRaw = sellAmount * selection.getValue();
        double sellValueRounded = Math.round(sellValueRaw*100.0)/100.0;
        bank.deposit(sellValueRounded);
        bank.getCurrentAccount().addTransaction(
            "Sold " + sellAmount + " of " + selection.getName() +
            " for $" + String.format("%.2f", sellValueRounded));
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
            System.out.println("    Value: $" + security.getValue());
            index++;
        }
        return true;
    }
}
