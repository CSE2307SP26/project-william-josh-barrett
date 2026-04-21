package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import main.CheckingAccount;

public class CheckingAccountTest {

    @Test
    public void testGetName() {
        CheckingAccount acc = new CheckingAccount("John");
        assertEquals("John", acc.getName());
    }

    @Test
    public void testInitialBalanceIsZero() {
        CheckingAccount acc = new CheckingAccount("John");
        assertEquals(0, acc.getBalance(), 0.0001);
    }

    // Daily Transaction Limit Tests
    @Test
    public void testDefaultDailyTransactionLimit() {
        CheckingAccount acc = new CheckingAccount("John");
        assertEquals(1000.00, acc.getDailyTransactionLimit(), 0.0001);
    }

    @Test
    public void testCustomDailyTransactionLimit() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        assertEquals(500.00, acc.getDailyTransactionLimit(), 0.0001);
    }

    @Test
    public void testSetDailyTransactionLimit() {
        CheckingAccount acc = new CheckingAccount("John");
        acc.setDailyTransactionLimit(2000.00);
        assertEquals(2000.00, acc.getDailyTransactionLimit(), 0.0001);
    }

    @Test
    public void testInvalidDailyTransactionLimitNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CheckingAccount("John", -100.00);
        });
    }

    @Test
    public void testInvalidDailyTransactionLimitZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CheckingAccount("John", 0);
        });
    }

    @Test
    public void testInvalidDailyTransactionLimitSetNegative() {
        CheckingAccount acc = new CheckingAccount("John");
        assertThrows(IllegalArgumentException.class, () -> {
            acc.setDailyTransactionLimit(-500.00);
        });
    }

    @Test
    public void testInvalidDailyTransactionLimitSetZero() {
        CheckingAccount acc = new CheckingAccount("John");
        assertThrows(IllegalArgumentException.class, () -> {
            acc.setDailyTransactionLimit(0);
        });
    }

    // Daily Withdrawal Total Tests
    @Test
    public void testInitialDailyWithdrawalTotal() {
        CheckingAccount acc = new CheckingAccount("John");
        assertEquals(0, acc.getDailyWithdrawalTotal(), 0.0001);
    }

    @Test
    public void testGetRemainingDailyWithdrawal() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        assertEquals(500.00, acc.getRemainingDailyWithdrawal(), 0.0001);
    }

    @Test
    public void testGetRemainingDailyWithdrawalAfterWithdrawal() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(1000);
        acc.withdraw(200);
        assertEquals(300.00, acc.getRemainingDailyWithdrawal(), 0.0001);
    }

    @Test
    public void testResetDailyWithdrawalTotal() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(1000);
        acc.withdraw(200);
        assertEquals(200.00, acc.getDailyWithdrawalTotal(), 0.0001);
        acc.resetDailyWithdrawalTotal();
        assertEquals(0, acc.getDailyWithdrawalTotal(), 0.0001);
    }

    // Deposit Tests
    @Test
    public void testDeposit() {
        CheckingAccount acc = new CheckingAccount("John");
        acc.deposit(500);
        assertEquals(500, acc.getBalance(), 0.0001);
    }

    @Test
    public void testMultipleDeposits() {
        CheckingAccount acc = new CheckingAccount("John");
        acc.deposit(100);
        acc.deposit(200);
        acc.deposit(150);
        assertEquals(450, acc.getBalance(), 0.0001);
    }

    // Can Withdraw Tests
    @Test
    public void testCanWithdrawWithinLimit() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(1000);
        assertTrue(acc.canWithdraw(200));
    }

    @Test
    public void testCanWithdrawExceedsLimit() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(1000);
        assertFalse(acc.canWithdraw(600));
    }

    @Test
    public void testCanWithdrawExceedsBalance() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(300);
        assertFalse(acc.canWithdraw(400));
    }

    @Test
    public void testCanWithdrawExactLimit() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(1000);
        assertTrue(acc.canWithdraw(500));
    }

    // Withdraw Tests
    @Test
    public void testWithdrawWithinLimit() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(1000);
        acc.withdraw(200);
        assertEquals(800, acc.getBalance(), 0.0001);
    }

    @Test
    public void testWithdrawMultipleTimes() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(1000);
        acc.withdraw(100);
        acc.withdraw(150);
        acc.withdraw(75);
        assertEquals(675, acc.getBalance(), 0.0001);
        assertEquals(325.00, acc.getDailyWithdrawalTotal(), 0.0001);
    }

    @Test
    public void testWithdrawExceedsLimit() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(1000);
        assertThrows(IllegalArgumentException.class, () -> {
            acc.withdraw(600);
        });
    }

    @Test
    public void testWithdrawExceedsBalance() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(300);
        assertThrows(IllegalArgumentException.class, () -> {
            acc.withdraw(400);
        });
    }

    @Test
    public void testWithdrawNegativeAmount() {
        CheckingAccount acc = new CheckingAccount("John");
        acc.deposit(500);
        assertThrows(IllegalArgumentException.class, () -> {
            acc.withdraw(-50);
        });
    }

    @Test
    public void testWithdrawZeroAmount() {
        CheckingAccount acc = new CheckingAccount("John");
        acc.deposit(500);
        assertThrows(IllegalArgumentException.class, () -> {
            acc.withdraw(0);
        });
    }

    @Test
    public void testWithdrawExactLimit() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(1000);
        acc.withdraw(500);
        assertEquals(500, acc.getBalance(), 0.0001);
        assertEquals(500.00, acc.getDailyWithdrawalTotal(), 0.0001);
    }

    // Integration Tests
    @Test
    public void testMultipleLimitChanges() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(2000);
        acc.withdraw(400); // Withdrew 400/500

        // Increase limit
        acc.setDailyTransactionLimit(1000.00);
        acc.withdraw(300); // Should succeed, now 700/1000
        assertEquals(1300, acc.getBalance(), 0.0001);
    }

    @Test
    public void testResetAllowsMoreWithdrawals() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(2000);
        acc.withdraw(400);
        assertEquals(400.00, acc.getDailyWithdrawalTotal(), 0.0001);

        acc.resetDailyWithdrawalTotal();
        acc.withdraw(400); // Should succeed
        assertEquals(1200, acc.getBalance(), 0.0001);
        assertEquals(400.00, acc.getDailyWithdrawalTotal(), 0.0001);
    }

    @Test
    public void testWithdrawUpdatesTransactionHistory() {
        CheckingAccount acc = new CheckingAccount("John");
        acc.deposit(500);
        int historySize = acc.getTransactionHistory().size();
        acc.withdraw(100);
        // withdraw calls super.withdraw (adds 1) + addTransaction (adds 1) = 2 total
        assertEquals(historySize + 2, acc.getTransactionHistory().size());
    }

    @Test
    public void testDailyLimitEnforcementAcrossMultipleWithdrawals() {
        CheckingAccount acc = new CheckingAccount("John", 500.00);
        acc.deposit(2000);

        // Make multiple withdrawals approaching the limit
        acc.withdraw(200);
        assertEquals(1800, acc.getBalance(), 0.0001);
        assertEquals(200.00, acc.getDailyWithdrawalTotal(), 0.0001);

        acc.withdraw(200);
        assertEquals(1600, acc.getBalance(), 0.0001);
        assertEquals(400.00, acc.getDailyWithdrawalTotal(), 0.0001);

        // This should succeed (exactly at limit)
        acc.withdraw(100);
        assertEquals(1500, acc.getBalance(), 0.0001);
        assertEquals(500.00, acc.getDailyWithdrawalTotal(), 0.0001);

        // This should fail (exceeds limit)
        assertThrows(IllegalArgumentException.class, () -> {
            acc.withdraw(1);
        });
    }

    @Test
    public void testLargeAmountsWithinLimit() {
        CheckingAccount acc = new CheckingAccount("John", 5000.00);
        acc.deposit(10000);
        acc.withdraw(4500);
        assertEquals(5500, acc.getBalance(), 0.0001);
        assertEquals(4500.00, acc.getDailyWithdrawalTotal(), 0.0001);
    }

    @Test
    public void testSmallLimitEnforcement() {
        CheckingAccount acc = new CheckingAccount("John", 50.00);
        acc.deposit(200);
        acc.withdraw(30);
        acc.withdraw(20);
        assertThrows(IllegalArgumentException.class, () -> {
            acc.withdraw(5);
        });
    }
}
