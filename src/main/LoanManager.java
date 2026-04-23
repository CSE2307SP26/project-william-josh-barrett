package main;

public class LoanManager {
    
    private static final double NONE = 0.0;

    private IOUtils io;
    private BankManager bank;

    public LoanManager(IOUtils io, BankManager bank) {
        this.io = io;
        this.bank = bank;
    }

    public void open() {
        if (bank.getAmountOwed() == NONE) {
            newLoan();
        } else {
            payLoan();
        }
    }

    public void newLoan() {
        System.out.print("You have no outstanding loans. Borrow money? (Y/N): ");
        if (!io.scanAffirmative()) {
            return;
        }
        System.out.print("Enter the amount you would like to borrow: ");
        double amount = Math.round(io.scanPositiveDouble()*100)/100;
        double interest = Math.round((amount*0.05)*100)/100;
        System.out.print("Would you like to take out a loan for $" + amount + "?" 
                      + " Total interest: $" + interest + " (Y/N): ");
        if (!io.scanAffirmative()) {
            return;
        }
        bank.deposit(amount);
        bank.setAmountOwed(amount + interest);
        System.out.println("Loan successful.");
        bank.addTransaction("New loan worth $" + amount + " taken out, leaving $" + bank.getAmountOwed() + " owed.");
    }

    public void payLoan() {
        if (bank.getBalance() == NONE) {
            System.out.println("You have no funds available to pay off your loan.");
            return;
        }
        System.out.print("You have an outstanding balance due of $" + bank.getAmountOwed() + ". Pay it off? (Y/N): ");
        if (!io.scanAffirmative()) {
            return;
        }
        System.out.print("Enter the amount you would like to contribute: ");
        double amount = scanLoanPayment();
        bank.withdraw(amount);
        bank.setAmountOwed(bank.getAmountOwed() - amount);
        System.out.println("Loan payment successful.");
        bank.addTransaction("$" + amount + " contributed to an outstanding loan, leaving $" + bank.getAmountOwed() + " owed.");
    }

    public double scanLoanPayment() {
        while (true) {
            double amount = Math.round(io.scanPositiveDouble()*100)/100;
            if (amount <= bank.getBalance() && amount <= bank.getAmountOwed()) {
                return amount;
            }
            if (amount > bank.getBalance()) {
                System.out.println("You do not have the funds to cover this amount.");
            }
            if (amount > bank.getAmountOwed()) {
                System.out.println("This amount is larger than the amount you owe.");
            }
            System.out.print("Please try again: ");
        }
    }
}
