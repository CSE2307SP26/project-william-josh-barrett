package main;

/**
 * SavingsAccount represents a bank account specialized for saving money with
 * interest accrual. Users can view the interest rate, but only administrators
 * can modify it.
 * Key features:
 * - Fixed interest rate applied to account balance
 * - Interest payments can be added to the account
 * - Interest rate is configurable only by administrators
 */
public class SavingsAccount extends BankAccount {
    private double interestRate;
    private static final double DEFAULT_INTEREST_RATE = 0.02;
    private static final double MINIMUM_INTEREST_RATE = 0.0;
    private static final double MAXIMUM_INTEREST_RATE = 1.0;

    public SavingsAccount(String name) {
        super(name);
        this.interestRate = DEFAULT_INTEREST_RATE;
    }
    @Override
    public String getAccountType() {
        return "Savings";
    }

    SavingsAccount(String name, double interestRate) {
        super(name);
        validateInterestRate(interestRate);
        this.interestRate = interestRate;
    }

    /**
     * Get the current interest rate for this savings account.
     * This method is available to all users.
     * 
     * @return interest rate as a decimal (e.g., 0.02 for 2%)
     */
    public double getInterestRate() {
        return this.interestRate;
    }

    /**
     * Set the interest rate for this savings account (admin only).
     * 
     * @param interestRate the new interest rate (must be between 0 and 1)
     */
    void setInterestRate(double interestRate) {
        validateInterestRate(interestRate);
        this.interestRate = interestRate;
    }

    private void validateInterestRate(double interestRate) {
        if (interestRate < MINIMUM_INTEREST_RATE || interestRate > MAXIMUM_INTEREST_RATE) {
            throw new IllegalArgumentException(
                    "Interest rate must be between " + MINIMUM_INTEREST_RATE + " and " + MAXIMUM_INTEREST_RATE);
        }
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
     * 
     * @return the calculated interest amount
     */
    public double calculateInterestEarned() {
        return Math.round(this.getBalance() * interestRate * 100.0) / 100.0;
    }
}
