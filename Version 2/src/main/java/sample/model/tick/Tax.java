package sample.model.tick;

import sample.model.World;
import sample.model.agent.Agent;

public class Tax extends AbstractTick {

    private static float taxRate;
    private final float taxIncrement;

    public Tax(World world, float  taxRate, float taxIncrement) {
        super(world);
        Tax.taxRate = taxRate;
        this.taxIncrement = taxIncrement;
    }

    @Override
    public int tick() {
        taxRate += taxIncrement / 52;
        for (Agent agent: getAgents())
            agent.updateDataTax(getTick() + 1, getDataManager());
        return super.tick();
    }

    public static float getTaxRate() {
        return taxRate;
    }
}
