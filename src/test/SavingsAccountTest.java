package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import main.SavingsAccount;

public class SavingsAccountTest {

    @Test
    public void testGetName() {
        SavingsAccount acc = new SavingsAccount("John");
        assertEquals("John", acc.getName());
    }

    @Test
    public void testInitialBalanceIsZero() {
        SavingsAccount acc = new SavingsAccount("John");
        assertEquals(0, acc.getBalance(), 0.0001);
    }
}