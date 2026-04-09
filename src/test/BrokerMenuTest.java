package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import main.BankManager;
import main.BrokerMenu;
import main.MainMenu;
import main.Security;

public class BrokerMenuTest {

    public BrokerMenu setUpBroker() {
        BankManager bank = new BankManager();
        BrokerMenu broker = new BrokerMenu(new MainMenu(), bank);
        bank.createAccount("test");
        bank.switchAccounts(1);
        ArrayList<Security> portfolio = bank.getPortfolio();
        Security testSecurity = new Security("testSecurity", 20, 20);
        portfolio.add(testSecurity);
        broker.updatePortfolio();
        return broker;
    }
    
    @Test
    public void testGetSecurity() {
        BrokerMenu broker = setUpBroker();
        Security testSecurity = broker.getSecurity("testSecurity");
        assertEquals(testSecurity.getName(), "testSecurity");
    }

    @Test
    public void testCheckSecurityOwnership() {
        BrokerMenu broker = setUpBroker();
        assert(broker.checkSecurityOwnership("testSecurity"));
    }

    @Test
    public void testSellSecuritySome() {
        BrokerMenu broker = setUpBroker();
        Security testSecurity = broker.getSecurity("testSecurity");
        broker.sellSecurity(testSecurity, 15);
        assertEquals(testSecurity.getAmount(), 5);
    }

    @Test
    public void testSellSecurityAll() {
        BrokerMenu broker = setUpBroker();
        Security testSecurity = broker.getSecurity("testSecurity");
        broker.sellSecurity(testSecurity, 20);
        assertEquals(testSecurity.getAmount(), 0);
        assert(!broker.checkSecurityOwnership("testSecurity"));
    }
}
