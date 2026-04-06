package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.BankAccount;
import main.BankManager;

public class BankManagerTest {
    
    @Test
    public void testTransferFromEmpty() {
        BankManager bank = new BankManager();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        toAccount.deposit(10);
        assert(!bank.transferDirect(1, fromAccount, toAccount));
    }

    @Test
    public void testTransferToEmpty() {
        BankManager bank = new BankManager();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        bank.transferDirect(1, fromAccount, toAccount);
        assertEquals(fromAccount.getBalance(), 9.0, 0.0001);
        assertEquals(toAccount.getBalance(), 1.0, 0.0001);
    }

    @Test
    public void testTransferToExistingBalance() {
        BankManager bank = new BankManager();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        toAccount.deposit(10);
        bank.transferDirect(1, fromAccount, toAccount);
        assertEquals(fromAccount.getBalance(), 9.0, 0.0001);
        assertEquals(toAccount.getBalance(), 11.0, 0.0001);
    }

    @Test
    public void testTransferNegativeAmount() {
        BankManager bank = new BankManager();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        toAccount.deposit(10);
        assert(!bank.transferDirect(-1, fromAccount, toAccount));
    }

    @Test
    public void testTransferZeroAmount() {
        BankManager bank = new BankManager();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        toAccount.deposit(10);
        assert(!bank.transferDirect(0, fromAccount, toAccount));
    }

    @Test
    public void testTransferAll() {
        BankManager bank = new BankManager();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        toAccount.deposit(10);
        bank.transferDirect(10, fromAccount, toAccount);
        assertEquals(fromAccount.getBalance(), 0.0, 0.0001);
        assertEquals(toAccount.getBalance(), 20.0, 0.0001);
    }

    @Test
    public void testTransferAmountTooLarge() {
        BankManager bank = new BankManager();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        toAccount.deposit(10);
        assert(!bank.transferDirect(11, fromAccount, toAccount));
    }
}
