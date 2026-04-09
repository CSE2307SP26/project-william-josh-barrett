package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class BankAccountTest {

    @Test
    public void testInitialBalanceIsZero() {
        BankAccount testAccount = new BankAccount("newAccount");
        assertEquals(0, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testDeposit() {
        BankAccount testAccount = new BankAccount("");
        testAccount.deposit(50);
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidDeposit() {
        BankAccount testAccount = new BankAccount("");
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.deposit(-50);
        });
    }

    @Test
    public void testZeroDeposit() {
        BankAccount testAccount = new BankAccount("");
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.deposit(0);
        });
    }

    @Test
    public void testWithdraw() {
        BankAccount testAccount = new BankAccount("");
        testAccount.deposit(50);
        testAccount.withdraw(25.5);
        assertEquals(24.5, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testNegativeWithdraw() {
        BankAccount testAccount = new BankAccount("");
        testAccount.deposit(50);
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.withdraw(-50);
        });
    }

    @Test
    public void testZeroWithdraw() {
        BankAccount testAccount = new BankAccount("");
        testAccount.deposit(50);
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.withdraw(0);
        });
    }

    @Test
    public void testOverdraw() {
        BankAccount testAccount = new BankAccount("");
        testAccount.deposit(50);
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.withdraw(75);
        });
    }

    @Test
    public void testWithdrawEntireBalance() {
        BankAccount testAccount = new BankAccount("fullWithdraw");
        testAccount.deposit(50);
        testAccount.withdraw(50);
        assertEquals(0, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testMultipleWithdrawals() {
        BankAccount testAccount = new BankAccount("multipleWithdrawals");
        testAccount.deposit(100);
        testAccount.withdraw(25);
        testAccount.withdraw(25);
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testBalanceUnchangedAfterInvalidWithdraw() {
        BankAccount testAccount = new BankAccount("unchangedBalance");
        testAccount.deposit(50);
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.withdraw(75);
        });
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testAddInterestPayment() {
        BankAccount testAccount = new BankAccount("interestAccount");
        testAccount.deposit(100);
        testAccount.addInterestPayment(12.5);
        assertEquals(112.5, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidInterestPayment() {
        BankAccount testAccount = new BankAccount("interestAccount");
        testAccount.deposit(100);
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.addInterestPayment(0);
        });
    }

    @Test
    public void testSetAndGetPassword() {
        BankAccount testAccount = new BankAccount("passwordAccount");
        assertEquals(null, testAccount.getPassword());
        testAccount.setPassword("mySecurePin123");
        assertEquals("mySecurePin123", testAccount.getPassword());
    }

    @Test
    public void testGetName() {
        BankAccount testAccount = new BankAccount("testName");
        assertEquals(testAccount.getName(), "testName");
    }
}
