package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
        try {
            testAccount.deposit(-50);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
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
        try {
            testAccount.withdraw(-50);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
    }

    @Test
    public void testOverdraw() {
        BankAccount testAccount = new BankAccount("");
        testAccount.deposit(50);
        try {
            testAccount.withdraw(75);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
    }

    @Test
    public void testGetName() {
        BankAccount testAccount = new BankAccount("testName");
        assertEquals(testAccount.getName(), "testName");
    }
}
