package sample.model.tick;

import sample.model.World;
import sample.model.agent.Agent;

/**
 * Performs per tick actions for a tax model, inheriting from the abstract tick class
 */
public class Tax extends AbstractTick {

    private static float taxRate;       // The current tax rate
    private final float taxIncrement;   // The yearly increase in tax

    /**
     * Constructor, calls the AbstractTick constructor, sets the world, tax rate and increment
     * @param world         The world in which the simulation is running
     * @param taxRate       The initial tax rate
     * @param taxIncrement  The amount by which tax increases per year
     */
    public Tax(World world, float  taxRate, float taxIncrement) {
        super(world);
        Tax.taxRate = taxRate;
        this.taxIncrement = taxIncrement;
    }

    /**
     * Overridden method, Increments the tax rate by the weekly fraction of the tax increment variable, loops
     * through and updates agents.
     * @return The number of the tick that has transpired for comparison against a maximum tick
     */
    @Override
    public int tick() {
        taxRate += taxIncrement / 52;
        for (Agent agent: getAgents())
            agent.updateDataTax(getTick() + 1, getDataManager());
        return super.tick();
    }

    /**
     * Getter for the current tax rate
     * @return  The current tax rate
     */
    public static float getTaxRate() {
        return taxRate;
    }
}
