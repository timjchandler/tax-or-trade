package sample.model;

import sample.model.agent.Agent;

public class Tax extends AbstractTick {

    private static float taxRate;
    private final float taxIncrement;

    public Tax(World world, float  taxRate, float taxIncrement) {
        super(world);
        this.taxRate = taxRate;
        this.taxIncrement = taxIncrement;
    }

    @Override
    public int tick() {
        taxRate += taxIncrement / 52;
        for (Agent agent: getAgents()) agent.updateData(getTick() + 1, getDataManager());
        return super.tick();
    }

    public static float getTaxRate() {
        return taxRate;
    }
}
