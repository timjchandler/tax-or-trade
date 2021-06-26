package sample.model.power;

import sample.model.Randomiser;
import sample.model.tick.Tax;
import sample.model.World;
import sample.model.tick.Trade;

public class Power extends Randomiser {

    private final PowerType type;   // The type of power (Coal, gas, wind, nuclear)
    private float runningCost;      // The running cost of the power plant
    private float production;       // The amount of power produced
    private float carbon;           // The amount of carbon produced
    private int idleTime = 0;
    private boolean used = false;

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
        this.carbon = production * getNormal(type.getMeanCarbon()) * this.production;
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

    public float normalisedIncomeFromTax() {
        return calculateIncomeFromTax() / production;
    }

    public float normalisedIncomeFromTrade() {
        float revenue = production * World.getEnergyPrice();
        float costs = carbon * Trade.getCreditPrice() + runningCost;
        return (revenue - costs) / production;
    }

    public float carbonNormalisedIncome() {
        return (production * World.getEnergyPrice()) / carbon;
    }

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

    public boolean isUsed() {
        return used;
    }

    public void use() {
        used = true;
    }

    public void clearUse() {
        used = false;
    }
}
