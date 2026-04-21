package main;

/**
 * SavingsAccount represents a bank account specialized for saving money with interest accrual.
 * Key features:
 * - Fixed interest rate applied to account balance
 * - Interest payments can be added to the account
 * - Interest rate is configurable
 */
public class SavingsAccount extends BankAccount {
    private double interestRate;
    private static final double DEFAULT_INTEREST_RATE = 0.02; // 2% annual interest rate

    public SavingsAccount(String name) {
        super(name);
        this.interestRate = DEFAULT_INTEREST_RATE;
    }

    public SavingsAccount(String name, double interestRate) {
        super(name);
        if (interestRate < 0 || interestRate > 1.0) {
            throw new IllegalArgumentException("Interest rate must be between 0 and 1");
        }
        this.interestRate = interestRate;
    }

    /**
     * Get the current interest rate for this savings account
     * @return interest rate as a decimal (e.g., 0.02 for 2%)
     */
    public double getInterestRate() {
        return this.interestRate;
    }

    /**
     * Set the interest rate for this savings account
     * @param interestRate the new interest rate (must be between 0 and 1)
     */
    public void setInterestRate(double interestRate) {
        if (interestRate < 0 || interestRate > 1.0) {
            throw new IllegalArgumentException("Interest rate must be between 0 and 1");
        }
        this.interestRate = interestRate;
    }

    /**
     * Calculate and add interest payment based on current balance and interest rate
     * Interest is calculated as: balance * interestRate
     */
    public void applyInterest() {
        double interestAmount = Math.round(this.getBalance() * interestRate * 100.0) / 100.0;
        if (interestAmount > 0) {
            this.addInterestPayment(interestAmount);
            this.addTransaction("Interest earned: $" + interestAmount + ". New balance: $" + this.getBalance());
        }
    }

    /**
     * Calculate the interest that would be earned on the current balance
     * @return the calculated interest amount
     */
    public double calculateInterestEarned() {
        return Math.round(this.getBalance() * interestRate * 100.0) / 100.0;
    }
}
