package tjc.model.power;

import tjc.model.Randomiser;
import tjc.model.World;
import tjc.model.tick.Tax;

public class Power extends Randomiser {

    private final PowerType type;   // The type of power (seventy, gas, wind, nuclear)
    private float runningCost;      // The running cost of the power plant
    private float production;       // The amount of power produced
    private float carbon;           // The amount of carbon produced
    private int idleTime = 0;
    private boolean usedThisTick;

    /**
     * Constructor, sets the power type and calls the initialise method
     * @param type      The power type used by this power object
     */
    public Power(PowerType type) {
        this.type = type;
        initPower();
    }

    /**
     * Initialise the object. Sets the running cost, production amount and idle cost for
     * the object taking values randomly from the normal curves for each variable
     */
    private void initPower() {
        this.production = getNormal(type.getMeanPower());
        this.runningCost = getNormal(type.getMeanCost(), 0) * this.production;
        this.carbon = getNormal(type.getMeanCarbon()) * this.production;
        this.usedThisTick = false;
        resetIdle();
    }

    /**
     * Returns the potential income from this power plant based on the current tax
     * rate and the carbon emission factor of the power plant
     * @return      The potential income
     */
    public float calculateIncomeFromTax() {
        float revenue = production * World.getEnergyPrice();
        float costs = carbon * Tax.getTaxRate() + runningCost;
        return revenue - costs;
    }

    public float calculateIncomeFromTrade() {
        float revenue = production * World.getEnergyPrice();
        float costs = runningCost;
        return revenue - costs;
    }

    /**
     * Calculates the income at a current tax rate normalised per GWh of electricity produced
     * @return The normalised income
     */
    public float normalisedIncomeFromTax() {
        return calculateIncomeFromTax() / production;
    }

    /**
     * Calculates the income of the plant per unit of CO2
     * @return the normalised income
     */
    public float carbonNormalisedIncome() {
        return World.getEnergyPrice() / (carbon / production);
    }

    /**
     * Increments a counter for how long the power plant has been idle
     * @return  True if the power plant has been idle for less than 10 ticks
     */
    public boolean decayIdle() {
        return idleTime++ < 10;
    }

    /**
     * Resets the idle cost to the default value
     */
    public void resetIdle() {
        idleTime = 0;
    }

    /**
     * Gets the type of power generation
     * @return The power type
     */
    public PowerType getType() {
        return type;
    }

    /**
     * Gets the running cost of the power plant
     * @return The running cost
     */
    public float getRunningCost() {
        return runningCost;
    }

    /**
     * Gets the amount of power produced by the plant
     * @return The amount of power produced
     */
    public float getProduction() {
        return production;
    }

    /**
     * Getter for the carbon dioxide produced by the power plant
     * @return The amount of carbon dioxide produced
     */
    public float getCarbon() {
        return carbon;
    }

    /**
     * Getter returns true if the power plant was used this tick, else false
     * @return whether the power plant was used this tick
     */
    public boolean isUsedThisTick() {
        return usedThisTick;
    }

    /**
     * Marks the plant as used or unused for a given tick
     * @param usedThisTick whether the power plant was used
     */
    public void setUsedThisTick(boolean usedThisTick) {
        this.usedThisTick = usedThisTick;
    }

    public void resetUsedThisTick() {
        usedThisTick = false;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public float getUnusedCost() {
        return runningCost * 0.5f - idleTime * 0.05f;
    }
}
