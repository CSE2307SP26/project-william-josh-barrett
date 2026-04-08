package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import main.BankAccount;
import main.BankManager;
import main.BrokerMenu;
import main.MainMenu;
import main.Security;

public class BrokerMenuTest {
    
    @Test
    public void testGetSecurity() {
        BankManager bank = new BankManager();
        BrokerMenu broker = new BrokerMenu(new MainMenu(), bank);
        bank.createAccount("test");
        bank.switchAccounts(1);
        ArrayList<Security> portfolio = bank.getPortfolio();
        Security testSecurity = new Security("testSecurity", 20, 20);
        portfolio.add(testSecurity);
        broker.updatePortfolio();
        assertEquals(broker.getSecurity("testSecurity"), testSecurity);
    }

    @Test
    public void testCheckSecurityOwnership() {
        BankManager bank = new BankManager();
        BrokerMenu broker = new BrokerMenu(new MainMenu(), bank);
        bank.createAccount("test");
        bank.switchAccounts(1);
        ArrayList<Security> portfolio = bank.getPortfolio();
        Security testSecurity = new Security("testSecurity", 20, 20);
        portfolio.add(testSecurity);
        broker.updatePortfolio();
        assert(broker.checkSecurityOwnership("testSecurity"));
    }

}
