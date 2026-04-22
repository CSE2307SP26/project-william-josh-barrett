package main;

public class Short extends Security {
    
    public Short(String name, int amount, double value) {
        super(name, amount, value);
    }

    public double getShortValueRoundedWithMarketValue(double marketValue) {
        double shortValue = buyValue - marketValue;
        double shortValueRounded = Math.round(shortValue*100.0)/100.0;
        return shortValueRounded;
    }

    public double getShortValueRounded() {
        return getShortValueRoundedWithMarketValue(super.getValue());
    }

    public double getValue() {
        return getShortValueRounded();
    }

    public String getValueFormatted() {
        double marketValue = super.getValue();
        double shortValueRounded = getShortValueRoundedWithMarketValue(marketValue);
        return "    Market Value: $" + String.valueOf(marketValue)
           + "\n    Buy Value: $" + buyValue
           + "\n    Short Value: $" + String.valueOf(shortValueRounded);
    }
}
