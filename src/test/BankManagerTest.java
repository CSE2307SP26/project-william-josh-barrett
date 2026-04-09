package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.Test;

import main.BankAccount;
import main.BankManager;
import main.Security;

public class BankManagerTest {

    @Test
    public void testGetCurAccountName() {
        BankManager bank = new BankManager();
        bank.createAccount("test_name");
        bank.switchAccounts(1);
        assertEquals(bank.getCurAccountName(), "test_name");
    }

    @Test
    public void testGetBalance() {
        BankManager bank = new BankManager();
        bank.createAccount("test_name");
        bank.switchAccounts(1);
        bank.deposit(20);
        assertEquals(bank.getBalance(), 20, 0.0001);
    }

    @Test
    public void testSetAndCheckPassword() {
        BankManager bank = new BankManager();
        bank.createAccount("test_name");
        bank.switchAccounts(1);

        // Assume default password is null
        assert (bank.checkPassword(null));
        assert (!bank.checkPassword("wrongpass"));

        bank.setPassword("securepass");
        assert (bank.checkPassword("securepass"));
        assert (!bank.checkPassword("securepas"));
        assert (!bank.checkPassword(null));
    }

    @Test
    public void testGetSize() {
        BankManager bank = new BankManager();
        assertEquals(bank.getSize(), 1);
        bank.createAccount("test_1");
        assertEquals(bank.getSize(), 2);
        bank.createAccount("test_2");
        assertEquals(bank.getSize(), 3);
    }

    @Test
    public void testGetPortfolio() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        ArrayList<Security> portfolio = bank.getPortfolio();
        assert (portfolio.isEmpty());
        portfolio.add(new Security("testSecurity", 0, 0));
        ArrayList<Security> portfolio_refresh = bank.getPortfolio();
        assertEquals(portfolio_refresh.get(0).getName(), "testSecurity");
    }

    @Test
    public void testGetAccountBalance() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        bank.deposit(10);
        assertEquals(bank.getAccountBalance(1), 10, 0.0001);
        bank.createAccount("test_2");
        bank.switchAccounts(2);
        bank.deposit(20);
        assertEquals(bank.getAccountBalance(2), 20, 0.0001);
        bank.deposit(10);
        assertEquals(bank.getAccountBalance(2), 30, 0.0001);
    }

    @Test
    public void testCheckBankIsEmpty() {
        BankManager bank = new BankManager();
        assert (bank.checkBankIsEmpty());
        bank.createAccount("test_1");
        assert (bank.checkBankIsEmpty());
        bank.createAccount("test_2");
        assert (bank.checkBankIsEmpty());
        bank.switchAccounts(1);
        bank.deposit(10);
        assert (!bank.checkBankIsEmpty());
    }

    @Test
    public void testDepositValid() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.deposit(1);
        assertEquals(bank.getAccountBalance(1), 1, 0.0001);
    }

    @Test
    public void testDepositNegative() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        assertThrows(IllegalArgumentException.class, () -> {
            bank.deposit(-1);
        });
    }

    @Test
    public void testDepositZero() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        assertThrows(IllegalArgumentException.class, () -> {
            bank.deposit(0);
        });
    }

    @Test
    public void testWithdrawValid() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        bank.deposit(2);
        bank.withdraw(1);
        assertEquals(bank.getAccountBalance(1), 1, 0.0001);
    }

    @Test
    public void testWithdrawNegative() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        bank.deposit(2);
        assertThrows(IllegalArgumentException.class, () -> {
            bank.withdraw(-1);
        });
    }

    @Test
    public void testWithdrawZero() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        bank.deposit(2);
        assertThrows(IllegalArgumentException.class, () -> {
            bank.withdraw(0);
        });
    }

    @Test
    public void testWithdrawOverdraw() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        bank.deposit(2);
        assertThrows(IllegalArgumentException.class, () -> {
            bank.withdraw(3);
        });
    }

    @Test
    public void testIsNotLoggedIn() {
        BankManager bank = new BankManager();
        assert (!bank.isLoggedIn());
    }

    @Test
    public void testIsLoggedIn() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        assert (bank.isLoggedIn());
    }

    @Test
    public void testIsAdminAccountIndex() {
        BankManager bank = new BankManager();
        assert (bank.isAdminAccount(0));
        bank.createAccount("test_1");
        assert (!bank.isAdminAccount(1));
    }

    @Test
    public void testIsAdminAccountObject() {
        BankManager bank = new BankManager();
        assert (bank.isAdminAccount(bank.getAdminAccount()));
        assert (!bank.isAdminAccount(bank.createAccount("test_1")));
    }

    @Test
    public void testIsAdminLoggedIn() {
        BankManager bank = new BankManager();
        assert (!bank.isAdminLoggedIn());
        bank.switchAccounts(0);
        assert (bank.isAdminLoggedIn());
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        assert (!bank.isAdminLoggedIn());
    }

    @Test
    public void testGetAdminAccount() {
        BankManager bank = new BankManager();
        bank.switchAccounts(0);
        assertEquals(bank.getCurAccountName(), "admin");
        assertEquals(bank.getAdminAccount().getName(), "admin");
    }

    @Test
    public void testTransferIndex() {
        BankManager bank = new BankManager();
        bank.createAccount("fromAccount");
        bank.createAccount("toAccount");
        bank.switchAccounts(1);
        bank.deposit(20);
        bank.transferIndex(10, 1, 2);
        assertEquals(bank.getBalance(), 10, 0.0001);
        bank.switchAccounts(2);
        assertEquals(bank.getBalance(), 10, 0.0001);
    }

    @Test
    public void testTransferFromEmpty() {
        BankManager bank = new BankManager();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("toAccount");
        toAccount.deposit(10);
        assert (!bank.transferDirect(1, fromAccount, toAccount));
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
        assert (!bank.transferDirect(-1, fromAccount, toAccount));
    }

    @Test
    public void testTransferZeroAmount() {
        BankManager bank = new BankManager();
        BankAccount fromAccount = new BankAccount("fromAccount");
        BankAccount toAccount = new BankAccount("fromAccount");
        fromAccount.deposit(10);
        toAccount.deposit(10);
        assert (!bank.transferDirect(0, fromAccount, toAccount));
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
        assert (!bank.transferDirect(11, fromAccount, toAccount));
    }

    @Test
    public void testCreateAccount() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        assertEquals(bank.getCurAccountName(), "test_1");
        bank.createAccount("test_2");
        bank.switchAccounts(2);
        assertEquals(bank.getCurAccountName(), "test_2");
    }

    @Test
    public void testCloseAccount() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        assertEquals(bank.getSize(), 2);
        bank.closeAccount(1);
        assertEquals(bank.getSize(), 1);
    }

    @Test
    public void testSwitchAccounts() {
        BankManager bank = new BankManager();
        assertThrows(NullPointerException.class, () -> {
            bank.getCurAccountName();
        });
        bank.createAccount("test_1");
        bank.createAccount("test_2");
        bank.switchAccounts(1);
        assertEquals(bank.getCurAccountName(), "test_1");
        bank.switchAccounts(2);
        assertEquals(bank.getCurAccountName(), "test_2");
    }

    @Test
    public void testAddInterestPaymentValid() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        bank.addInterestPayment(1, 10);
        assertEquals(bank.getBalance(), 10, 0.0001);
    }

    @Test
    public void testAddInterestPaymentZero() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        assertThrows(IllegalArgumentException.class, () -> {
            bank.addInterestPayment(1, 0);
        });
    }

    @Test
    public void testAddInterestPaymentNegative() {
        BankManager bank = new BankManager();
        bank.createAccount("test_1");
        bank.switchAccounts(1);
        assertThrows(IllegalArgumentException.class, () -> {
            bank.addInterestPayment(1, -10);
        });
    }

    @Test
    public void testGetCustomerAccounts() {
        ArrayList<BankAccount> realList = new ArrayList<BankAccount>();
        BankManager bank = new BankManager();
        realList.add(bank.createAccount("test_1"));
        realList.add(bank.createAccount("test_2"));
        assertEquals(realList, bank.getCustomerAccounts());
    }
}
