package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import main.BankAccount;
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

    @Test
    public void testGetAccountType() {
        BankAccount testAccount = new BankAccount("testName");
        assertEquals(testAccount.getAccountType(), "Savings");
    }

}