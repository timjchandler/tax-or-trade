package sample.model;

public class AgentStats {

    private float carbonTot;
    private float moneyTot;
    private float electricityTot;
    private float carbonTick;
    private float moneyTick;
    private float electricityTick;

    public AgentStats(float initialMoney) {
        carbonTot = carbonTick = 0;
        electricityTot = electricityTick = 0;
        moneyTick = 0;
        moneyTot = initialMoney;
    }

    public void updateElectricity(float value) {
        electricityTick = value;
        electricityTot += value;
    }

    public void updateCarbon(float value) {
        carbonTick = value;
        carbonTot += value;
    }

    public void updateMoney(float value) {
        moneyTick = value;
        moneyTot += value;
    }

    public float getCarbonTot() {
        return carbonTot;
    }

    public float getMoneyTot() {
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
        return sb.toString();
    }
}
