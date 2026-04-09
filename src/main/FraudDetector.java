package main;

public class FraudDetector {

    private static final double withdrawl_limit = 1000.0;
    private static final double transfer_limit = 2000.0;

    public String detectFraud(String transactionType, double amount) {
        if (transactionType.equalsIgnoreCase("withdraw") && amount > withdrawl_limit) {
            return "Suspicious withdrawal over $" + withdrawl_limit + ": $" + amount;
        }

        if (transactionType.equalsIgnoreCase("transfer") && amount > transfer_limit) {
            return "Suspicious transfer over $" + transfer_limit + ": $" + amount;
        }

        return null;
    }
}