package sample.model.power;

import sample.model.Randomiser;
import sample.model.World;
import sample.model.agent.Agent;

public class Power extends Randomiser {

    private final PowerType type;   // The type of power (Coal, gas, wind, nuclear)
    private float runningCost;      // The running cost of the power plant
    private float idleCost;         // The upkeep cost when not running the plant
    private float production;       // The amount of power produced
    private float carbon;           // The amount of carbon produced
    private Agent agent;            // The agent that this power plant belongs to
    private int tick = -1;          // The last tick this power was updated on
    private boolean active = false; // True if the power plant was used this tick, else false

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
    public float calculateIncome() {
        active = true;
        float revenue = production * World.getEnergyPrice();
        float costs = carbon * World.getTaxRate() * production + runningCost;
        return revenue - costs;
    }

    /**
     * Decays the cost of running the plant on idle. Will not reduce the idle cost below
     * 10% of running cost
     */
    public void decayIdle() {
        active = false;
        if (idleCost > runningCost * 0.1f) idleCost *= 0.9;
    }

    /**
     * Resets the idle cost to the default value
     */
    public void resetIdle() {
        idleCost = runningCost * type.getUpkeepWeight();
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
     * Get the idle cost of the power plant
     * @return The idle cost
     */
    public float getIdleCost() {
        return idleCost;
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
     * Overrides the inbuilt toString method to return the Power class in CSV format
     * @return The power class as a line of CSV
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(type.toString());
        sb.append("\tRunning Cost: ").append(runningCost);
        sb.append("\tIdle cost: ").append(idleCost);
        sb.append("\tProduction: ").append(production);
        return sb.toString();
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public void updateToAgent(int tick) {
        this.tick = tick;
        agent.updateFromPower(this, tick);
    }

    public int getTick() {
        return tick;
    }

    public boolean isActive() {
        return active;
    }

    public String verbose() {
        StringBuilder sb = new StringBuilder();
        sb.append(agent.getId()).append(',');
        sb.append(tick).append(',');
        sb.append(type.toString()).append(',');
        sb.append(carbon).append(',');
        sb.append(production).append('\n');
        return sb.toString();
    }

}
