package main;

import java.util.Random;

public class Security {

    private String name;
    private int amount;
    private double curValue;
    protected double buyValue;
    private Random rng;
    
    public Security(String name, int amount, double value) {
        this.name = name;
        this.amount = amount;
        this.curValue = value;
        this.buyValue = value;
        this.rng = new Random();
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int newAmount) {
        amount = newAmount;
    }

    public double getValue() {
        double newValueMin = curValue - (buyValue*0.05);
        double newValueRaw = newValueMin + ((buyValue*0.1)*rng.nextDouble());
        double newValueCents = Math.round(newValueRaw*100.0)/100.0;
        curValue = newValueCents;
        if (curValue <= 0) {
            curValue = 0;
            buyValue = 0;
        }
        return curValue;
    }

    public String getValueFormatted() {
        return "    Value: $" + String.valueOf(getValue());
    }
}
