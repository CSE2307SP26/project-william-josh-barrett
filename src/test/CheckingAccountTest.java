package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import main.BankAccount;
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

    // Daily Withdrawal Total Tests
    @Test
    public void testInitialDailyWithdrawalTotal() {
        CheckingAccount acc = new CheckingAccount("John");
        assertEquals(0, acc.getDailyWithdrawalTotal(), 0.0001);
    }

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
        CheckingAccount acc = new CheckingAccount("John");
        acc.deposit(1000);
        assertTrue(acc.canWithdraw(200));
    }

    @Test
    public void testCanWithdrawExceedsLimit() {
        CheckingAccount acc = new CheckingAccount("John");
        acc.deposit(2000);
        assertFalse(acc.canWithdraw(1500));
    }

    @Test
    public void testWithdrawWithinLimit() {
        CheckingAccount acc = new CheckingAccount("John");
        acc.deposit(1000);
        acc.withdraw(200);
        assertEquals(800, acc.getBalance(), 0.0001);
    }

    @Test
    public void testGetAccountType() {
        BankAccount testAccount = new BankAccount("testName");
        assertEquals(testAccount.getAccountType(), "Checking");
    }
}
