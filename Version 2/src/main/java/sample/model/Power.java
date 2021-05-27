package sample.model;

public class Power {

    private enum Type { GAS, WIND, COAL, NUCLEAR };

    private Type type;
    private float runningCost;
    private float idleCost;
    private float production;

    public Power(int value) {
        if (value < 0.5) this.type = Type.COAL;
        if (value >= 0.5) this.type = Type.WIND;
        initPower();
    }

    private void initPower() {

    }
}
