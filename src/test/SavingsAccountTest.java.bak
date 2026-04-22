package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import main.SavingsAccount;

public class SavingsAccountTest {

    @Test
    public void testGetName() {
        SavingsAccount acc = new SavingsAccount("John");
        assertEquals("John", acc.getName());
    }

    @Test
    public void testInitialBalanceIsZero() {
        SavingsAccount acc = new SavingsAccount("John");
        assertEquals(0, acc.getBalance(), 0.0001);
    }

    // Interest Rate Tests
    @Test
    public void testDefaultInterestRate() {
        SavingsAccount acc = new SavingsAccount("John");
        assertEquals(0.02, acc.getInterestRate(), 0.0001);
    }

    @Test
    public void testCustomInterestRate() {
        SavingsAccount acc = new SavingsAccount("John", 0.05);
        assertEquals(0.05, acc.getInterestRate(), 0.0001);
    }

    @Test
    public void testSetInterestRate() {
        SavingsAccount acc = new SavingsAccount("John");
        acc.setInterestRate(0.03);
        assertEquals(0.03, acc.getInterestRate(), 0.0001);
    }

    @Test
    public void testInvalidInterestRateNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SavingsAccount("John", -0.01);
        });
    }

    @Test
    public void testInvalidInterestRateAboveOne() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SavingsAccount("John", 1.5);
        });
    }

    @Test
    public void testInvalidInterestRateSetNegative() {
        SavingsAccount acc = new SavingsAccount("John");
        assertThrows(IllegalArgumentException.class, () -> {
            acc.setInterestRate(-0.01);
        });
    }

    @Test
    public void testInvalidInterestRateSetAboveOne() {
        SavingsAccount acc = new SavingsAccount("John");
        assertThrows(IllegalArgumentException.class, () -> {
            acc.setInterestRate(1.1);
        });
    }

    // Deposit and Withdrawal Tests
    @Test
    public void testDeposit() {
        SavingsAccount acc = new SavingsAccount("John");
        acc.deposit(100);
        assertEquals(100, acc.getBalance(), 0.0001);
    }

    @Test
    public void testMultipleDeposits() {
        SavingsAccount acc = new SavingsAccount("John");
        acc.deposit(100);
        acc.deposit(50);
        acc.deposit(25);
        assertEquals(175, acc.getBalance(), 0.0001);
    }

    @Test
    public void testWithdraw() {
        SavingsAccount acc = new SavingsAccount("John");
        acc.deposit(100);
        acc.withdraw(30);
        assertEquals(70, acc.getBalance(), 0.0001);
    }

    @Test
    public void testWithdrawMoreThanBalance() {
        SavingsAccount acc = new SavingsAccount("John");
        acc.deposit(100);
        assertThrows(IllegalArgumentException.class, () -> {
            acc.withdraw(150);
        });
    }

    @Test
    public void testNegativeWithdraw() {
        SavingsAccount acc = new SavingsAccount("John");
        acc.deposit(100);
        assertThrows(IllegalArgumentException.class, () -> {
            acc.withdraw(-10);
        });
    }

    // Interest Calculation Tests
    @Test
    public void testCalculateInterestEarned() {
        SavingsAccount acc = new SavingsAccount("John", 0.10);
        acc.deposit(100);
        double expectedInterest = 10.00; // 100 * 0.10
        assertEquals(expectedInterest, acc.calculateInterestEarned(), 0.0001);
    }

    @Test
    public void testCalculateInterestEarnedZeroBalance() {
        SavingsAccount acc = new SavingsAccount("John", 0.10);
        assertEquals(0, acc.calculateInterestEarned(), 0.0001);
    }

    @Test
    public void testCalculateInterestEarnedWithCustomRate() {
        SavingsAccount acc = new SavingsAccount("John", 0.05);
        acc.deposit(1000);
        double expectedInterest = 50.00; // 1000 * 0.05
        assertEquals(expectedInterest, acc.calculateInterestEarned(), 0.0001);
    }

    // Apply Interest Tests
    @Test
    public void testApplyInterest() {
        SavingsAccount acc = new SavingsAccount("John", 0.10);
        acc.deposit(100);
        acc.applyInterest();
        double expectedBalance = 110.00; // 100 + (100 * 0.10)
        assertEquals(expectedBalance, acc.getBalance(), 0.0001);
    }

    @Test
    public void testApplyInterestMultipleTimes() {
        SavingsAccount acc = new SavingsAccount("John", 0.10);
        acc.deposit(100);
        acc.applyInterest(); // Now 110
        acc.applyInterest(); // Now 121
        double expectedBalance = 121.00; // 100 + 10 + 11
        assertEquals(expectedBalance, acc.getBalance(), 0.0001);
    }

    @Test
    public void testApplyInterestZeroBalance() {
        SavingsAccount acc = new SavingsAccount("John", 0.10);
        acc.applyInterest(); // Should not crash
        assertEquals(0, acc.getBalance(), 0.0001);
    }

    @Test
    public void testApplyInterestUpdatesTransactionHistory() {
        SavingsAccount acc = new SavingsAccount("John", 0.10);
        acc.deposit(100);
        int historySize = acc.getTransactionHistory().size();
        acc.applyInterest();
        // applyInterest calls addInterestPayment (adds via deposit) + addTransaction (adds explicitly)
        assertEquals(historySize + 2, acc.getTransactionHistory().size());
    }

    // Integration Tests
    @Test
    public void testDepositAndApplyInterestMultipleTimes() {
        SavingsAccount acc = new SavingsAccount("John", 0.05);
        acc.deposit(1000);
        acc.applyInterest(); // 1050
        acc.deposit(500); // 1550
        acc.applyInterest(); // 1627.50
        double expectedBalance = 1627.50;
        assertEquals(expectedBalance, acc.getBalance(), 0.0001);
    }

    @Test
    public void testDepositWithdrawAndApplyInterest() {
        SavingsAccount acc = new SavingsAccount("John", 0.10);
        acc.deposit(500);
        acc.withdraw(100); // 400
        acc.applyInterest(); // 440
        double expectedBalance = 440.00;
        assertEquals(expectedBalance, acc.getBalance(), 0.0001);
    }

    @Test
    public void testInterestWithDecimalPlaces() {
        SavingsAccount acc = new SavingsAccount("John", 0.03);
        acc.deposit(333.33);
        double expectedInterest = 10.00; // 333.33 * 0.03 = 9.9999 ≈ 10.00
        assertEquals(expectedInterest, acc.calculateInterestEarned(), 0.01);
    }
}