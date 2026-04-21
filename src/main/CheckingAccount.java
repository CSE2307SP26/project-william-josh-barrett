package main;

/**
 * CheckingAccount represents a bank account designed for frequent transactions.
 * Users can view their daily transaction limit, but only administrators can
 * modify it. Key features:
 * - Daily transaction limit to prevent excessive withdrawals
 * - Withdrawal validation against daily limit
 * - Transaction limit is configurable only by administrators
 */
public class CheckingAccount extends BankAccount {
    private double dailyTransactionLimit;
    private double dailyWithdrawalTotal;
    private static final double DEFAULT_DAILY_LIMIT = 1000.00;
    private static final double MINIMUM_DAILY_LIMIT = 0.01;

    public CheckingAccount(String name) {
        super(name);
        this.dailyTransactionLimit = DEFAULT_DAILY_LIMIT;
        this.dailyWithdrawalTotal = 0;
    }

    CheckingAccount(String name, double dailyTransactionLimit) {
        super(name);
        validateDailyLimit(dailyTransactionLimit);
        this.dailyTransactionLimit = dailyTransactionLimit;
        this.dailyWithdrawalTotal = 0;
    }

    /**
     * Get the daily transaction limit for this checking account.
     * This method is available to all users.
     * 
     * @return the daily limit amount
     */
    public double getDailyTransactionLimit() {
        return this.dailyTransactionLimit;
    }

    /**
     * Set the daily transaction limit for this checking account (admin only).
     * 
     * @param dailyTransactionLimit the new daily limit (must be positive)
     */
    void setDailyTransactionLimit(double dailyTransactionLimit) {
        validateDailyLimit(dailyTransactionLimit);
        this.dailyTransactionLimit = dailyTransactionLimit;
    }

    private void validateDailyLimit(double dailyTransactionLimit) {
        if (dailyTransactionLimit < MINIMUM_DAILY_LIMIT) {
            throw new IllegalArgumentException(
                    "Daily transaction limit must be at least " + MINIMUM_DAILY_LIMIT);
        }
    }

    /**
     * Get the total amount withdrawn today
     * 
     * @return today's withdrawal total
     */
    public double getDailyWithdrawalTotal() {
        return this.dailyWithdrawalTotal;
    }

    /**
     * Get the remaining withdrawal amount allowed for today
     * 
     * @return the remaining withdrawal capacity
     */
    public double getRemainingDailyWithdrawal() {
        return Math.round((dailyTransactionLimit - dailyWithdrawalTotal) * 100.0) / 100.0;
    }

    /**
     * Reset the daily withdrawal total (typically called at end of day)
     */
    public void resetDailyWithdrawalTotal() {
        this.dailyWithdrawalTotal = 0;
        this.addTransaction("Daily withdrawal limit reset. Limit: $" + dailyTransactionLimit);
    }

    /**
     * Check if a withdrawal amount would exceed the daily limit
     * 
     * @param amount the withdrawal amount to check
     * @return true if the withdrawal is within the daily limit, false otherwise
     */
    public boolean canWithdraw(double amount) {
        return (dailyWithdrawalTotal + amount) <= dailyTransactionLimit && amount <= getBalance();
    }

    /**
     * Override withdraw to enforce daily transaction limits
     * 
     * @param amount the amount to withdraw
     * @throws IllegalArgumentException if amount would exceed daily limit or
     *                                  balance
     */
    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > getBalance()) {
            throw new IllegalArgumentException("Insufficient funds for withdrawal");
        }
        if (!canWithdraw(amount)) {
            throw new IllegalArgumentException(
                    "Withdrawal would exceed daily limit. Daily limit: $" + dailyTransactionLimit +
                            ", already withdrawn: $" + dailyWithdrawalTotal +
                            ", requested: $" + amount);
        }

        // Call parent withdraw method
        super.withdraw(amount);
        this.dailyWithdrawalTotal += amount;
        this.dailyWithdrawalTotal = Math.round(dailyWithdrawalTotal * 100.0) / 100.0;
        this.addTransaction("Daily withdrawal total: $" + dailyWithdrawalTotal + " / $" + dailyTransactionLimit);
    }
}
