package sample.model.tick;

import sample.model.tick.AbstractTick;
import sample.model.World;
import sample.model.agent.Agent;

public class Trade extends AbstractTick {

    private float cap;
    private float capChange;
    private static float creditPrice;

    public Trade(World world, float cap, float capChange) {
        super(world);
        this.cap = cap;
        this.capChange = capChange;
    }

    @Override
    public int tick() {
//        if (getTick() % 4 == 0) zeroCredits();
        cap -= capChange / 52;
        distributeCredits();
        return super.tick();
    }

    private void distributeCredits() {
        for (Agent agent: getAgents()) {
            int allowance = (int) (cap * agent.getTotalPotential() / getPossibleElectricity());
            agent.addCredits(allowance);
        }
    }

    private void zeroCredits() {
        for (Agent agent: getAgents()) agent.zeroCredits();
    }

    private float calculateBaseCreditPrice() {
        float total = 0;
        for (Agent agent: getAgents()) total += agent.getMeanCarbonValue();
        return total / getAgents().size();
    }

    public static float getCreditPrice() {
        return creditPrice;
    }

}
