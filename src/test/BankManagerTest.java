package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.BankAccount;
import main.MainMenu;

public class BankManagerTest {
    
    @Test
    public void testTransferFromEmpty() {
        MainMenu menu = new MainMenu();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        toAccount.deposit(10);
        assert(!menu.transferDirect(1, fromAccount, toAccount));
    }

    @Test
    public void testTransferToEmpty() {
        MainMenu menu = new MainMenu();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        menu.transferDirect(1, fromAccount, toAccount);
        assertEquals(fromAccount.getBalance(), 9.0, 0.0001);
        assertEquals(toAccount.getBalance(), 1.0, 0.0001);
    }

    @Test
    public void testTransferToExistingBalance() {
        MainMenu menu = new MainMenu();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        toAccount.deposit(10);
        menu.transferDirect(1, fromAccount, toAccount);
        assertEquals(fromAccount.getBalance(), 9.0, 0.0001);
        assertEquals(toAccount.getBalance(), 11.0, 0.0001);
    }

    @Test
    public void testTransferNegativeAmount() {
        MainMenu menu = new MainMenu();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        toAccount.deposit(10);
        assert(!menu.transferDirect(-1, fromAccount, toAccount));
    }

    @Test
    public void testTransferZeroAmount() {
        MainMenu menu = new MainMenu();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        toAccount.deposit(10);
        assert(!menu.transferDirect(0, fromAccount, toAccount));
    }

    @Test
    public void testTransferAll() {
        MainMenu menu = new MainMenu();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        toAccount.deposit(10);
        menu.transferDirect(10, fromAccount, toAccount);
        assertEquals(fromAccount.getBalance(), 0.0, 0.0001);
        assertEquals(toAccount.getBalance(), 20.0, 0.0001);
    }

    @Test
    public void testTransferAmountTooLarge() {
        MainMenu menu = new MainMenu();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        toAccount.deposit(10);
        assert(!menu.transferDirect(11, fromAccount, toAccount));
    }
}
