package sample.model.agent;

public class AgentStats {

    private float carbonTot;
    private double moneyTot;
    private float electricityTot;
    private float carbonTick;
    private float moneyTick;
    private float electricityTick;
    private int tick = -1;

    public AgentStats() {
        carbonTot = carbonTick = 0;
        electricityTot = electricityTick = 0;
        moneyTick = 0;
    }

    public void updateElectricity(float value, int tick) {
        if (tick == this.tick) electricityTick += value;
        else {
            electricityTick = value;
            carbonTick = 0;
            moneyTick = 0;
        }
        electricityTot += value;
        this.tick = tick;
    }

    public void updateCarbon(float value) {
        carbonTick += value;
        carbonTot += value;
    }

    public void updateMoney(float value) {
        moneyTick += value;
        moneyTot += value;
    }

    public void setMoneyTot(float moneyTot) {
        this.moneyTot = moneyTot;
    }

    public float getCarbonTot() {
        return carbonTot;
    }

    public double getMoneyTot() {
        return moneyTot;
    }

    public float getElectricityTot() {
        return electricityTot;
    }

    public float getCarbonTick() {
        return carbonTick;
    }

    public float getMoneyTick() {
        return moneyTick;
    }

    public float getElectricityTick() {
        return electricityTick;
    }

    /**
     * Returns the current ticks' values as a string in csv format in the order electricity, carbon, money
     * @return The current ticks' values as a string
     */
    public String tickToSring() {
        StringBuilder sb = new StringBuilder(String.valueOf(electricityTick));
        sb.append(',').append(carbonTick);
        sb.append(',').append(moneyTick);
        sb.append(',').append(moneyTot);
        return sb.toString();
    }
}
