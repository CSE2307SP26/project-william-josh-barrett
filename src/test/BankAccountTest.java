package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class BankAccountTest {

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
    public void testOverdraw() {
        BankAccount testAccount = new BankAccount("");
        testAccount.deposit(50);
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.withdraw(75);
        });
    }

    @Test
    public void testGetName() {
        BankAccount testAccount = new BankAccount("testName");
        assertEquals(testAccount.getName(), "testName");
    }

    @Test
    public void testCollectFee() {
        BankAccount testAccount = new BankAccount("feeAccount");
        testAccount.deposit(100);
        testAccount.collectFee(15.5);
        assertEquals(84.5, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testCollectInvalidFee() {
        BankAccount testAccount = new BankAccount("feeAccount");
        testAccount.deposit(100);
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.collectFee(-1);
        });
    }

    @Test
    public void testCollectFeeOverBalance() {
        BankAccount testAccount = new BankAccount("feeAccount");
        testAccount.deposit(100);
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.collectFee(150);
        });
    }
}
