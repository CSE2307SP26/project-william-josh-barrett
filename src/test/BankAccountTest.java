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
        assertThrows(IllegalArgumentException.class, ()->{testAccount.deposit(-50);});
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
        assertThrows(IllegalArgumentException.class, ()->{testAccount.withdraw(-50);});
    }

    @Test
    public void testOverdraw() {
        BankAccount testAccount = new BankAccount("");
        testAccount.deposit(50);
        assertThrows(IllegalArgumentException.class, ()->{testAccount.withdraw(75);});
    }

    @Test
    public void testGetName() {
        BankAccount testAccount = new BankAccount("testName");
        assertEquals(testAccount.getName(), "testName");
    }
}
