package sample.model;

public class Power extends Randomiser {

    private PowerType type;     // The type of power (Coal, gas, wind, nuclear)
    private float runningCost;  // The running cost of the power plant
    private float idleCost;     // The upkeep cost when not running the plant
    private float production;   // The amount of power produced
    private float carbon;       // The amount of carbon produced

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
        this.runningCost = getNormal(type.getMeanCost(), type.getSdCost());
        this.production = getNormal(type.getMeanPower());
        this.carbon = production * getNormal(type.getMeanCarbon());
        resetIdle();
    }

    public float calculateIncome(float tax) {
        return production * World.getEnergyPrice() - carbon * tax - runningCost;
    }

    public float calculateRunningIdleDifference(float tax) {
        return calculateIncome(tax) + idleCost;
    }

    /**
     * Decays the cost of running the plant on idle. Will not reduce the idle cost below
     * 10% of running cost
     */
    public void decayIdle() {
        if (idleCost > runningCost * 0.1f) idleCost *= 0.9;
    }

    /**
     * Resets the idle cost to the default value
     */
    public void resetIdle() {
        idleCost = runningCost * type.getUpkeepWeight();
    }

    // Getters
    public PowerType getType() {
        return type;
    }

    public float getRunningCost() {
        return runningCost;
    }

    public float getIdleCost() {
        return idleCost;
    }

    public float getProduction() {
        return production;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(type.toString());
        sb.append("\tRunning Cost: ").append(runningCost);
        sb.append("\tIdle cost: ").append(idleCost);
        sb.append("\tProduction: ").append(production);
        return sb.toString();
    }
}
