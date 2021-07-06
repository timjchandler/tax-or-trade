package tjc.model.agent;

/**
 * Manages the total and per tick values for CO2, electricity and money
 */
public class AgentStats {

    private float carbonTot;            // The total amount of CO2 produced in the model
    private double moneyTot;            // The total amount of money
    private float electricityTot;       // The total amount of electricity
    private float carbonTick;           // The CO2 produced per tick
    private float moneyTick;            // The money per tick
    private float electricityTick;      // The electricity per tick
    private int tick = -1;              // The current tick

    /**
     * Constructor sets all stored per tick values to zero
     */
    public AgentStats() {
        carbonTot = carbonTick = 0;
        electricityTot = electricityTick = 0;
        moneyTick = 0;
    }

    /**
     * Updates the electricity per tick and in total. If this is the first time this has been called
     * for a given tick, it resets the other per tick values to zero
     * @param value The amount of electricity to add
     * @param tick  The current tick
     */
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

    /**
     * Updates the stored carbon dioxide information per tick and in total
     * @param value The amount of CO2 to add
     */
    public void updateCarbon(float value) {
        carbonTick += value;
        carbonTot += value;
    }

    /**
     * Updates the total and per tick amounts of money
     * @param value The amount of money to add
     */
    public void updateMoney(float value) {
        moneyTick += value;
        moneyTot += value;
    }

    /**
     * Sets the total amount of money to a specific value
     * @param moneyTot  The amount of money to be set
     */
    public void setMoneyTot(float moneyTot) {
        this.moneyTot = moneyTot;
    }

    /**
     * Getter for the total CO2
     * @return The total CO2
     */
    public float getCarbonTot() {
        return carbonTot;
    }

    /**
     * Getter for the total money
     * @return The total money
     */
    public double getMoneyTot() {
        return moneyTot;
    }

    /**
     * Getter for the total electricity
     * @return The total electricity
     */
    public float getElectricityTot() {
        return electricityTot;
    }

    /**
     * Getter for the per tick CO2
     * @return the CO2 from the current tick
     */
    public float getCarbonTick() {
        return carbonTick;
    }

    /**
     * Getter for the per tick money
     * @return  The money for the current tick
     */
    public float getMoneyTick() {
        return moneyTick;
    }

    /**
     * Getter for the electricity per tick
     * @return  The electricity for the current tick
     */
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
