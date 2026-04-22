package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.Short;

public class ShortTest {
    
    @Test
    public void testGetName() {
        Short s = new Short("test", 0, 0);
        assertEquals(s.getName(), "test");
    }

    @Test
    public void testGetAmount() {
        Short s = new Short("", 20, 0);
        assertEquals(s.getAmount(), 20);
    }

    @Test
    public void testSetAmount() {
        Short s = new Short("", 0, 0);
        s.setAmount(20);
        assertEquals(s.getAmount(), 20);
    }

    @Test
    public void testGetValue() {
        for (int i = 0; i < 100; i++) {
            Short s = new Short("", 0, 20);
            double value = s.getValue();
            assert(value >= -1 && value <= 1);
        }
    }

    @Test
    public void testGetShortValueRoundedWithMarketValue() {
        Short s = new Short("", 0, 20);
        double value = s.getShortValueRoundedWithMarketValue(19);
        assert(value == 1);
    }

    @Test
    public void testGetValueFormatted() {
        Short s = new Short("", 1, 20);
        assert(s.getValueFormatted().contains("    Market Value: $"));
        assert(s.getValueFormatted().contains("    Buy Value: $"));
        assert(s.getValueFormatted().contains("    Short Value: $"));
    }

}
