package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import main.CheckingAccount;

public class CheckingAccountTest {

    @Test
    public void testGetName() {
        CheckingAccount acc = new CheckingAccount("John");
        assertEquals("John", acc.getName());
    }

    @Test
    public void testInitialBalanceIsZero() {
        CheckingAccount acc = new CheckingAccount("John");
        assertEquals(0, acc.getBalance(), 0.0001);
    }
}
