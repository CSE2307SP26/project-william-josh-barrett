package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.Security;

public class SecurityTest {
    
    @Test
    public void testGetName() {
        Security s = new Security("test", 0, 0);
        assertEquals(s.getName(), "test");
    }

    @Test
    public void testGetAmount() {
        Security s = new Security("", 20, 0);
        assertEquals(s.getAmount(), 20);
    }

    @Test
    public void testSetAmount() {
        Security s = new Security("", 0, 0);
        s.setAmount(20);
        assertEquals(s.getAmount(), 20);
    }

    @Test
    public void testGetValue() {
        for (int i = 0; i < 100; i++) {
            Security s = new Security("", 0, 20);
            double value = s.getValue();
            assert(value >= 19 && value <= 21);
        }
    }

    @Test
    public void testGetValueFormatted() {
        Security s = new Security("", 1, 20);
        assert(s.getValueFormatted().contains("    Value: $"));
    }

}
