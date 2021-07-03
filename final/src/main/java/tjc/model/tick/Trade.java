package tjc.model.tick;

import tjc.model.World;
import tjc.model.agent.Agent;

public class Trade extends AbstractTick {

    private float cap;
    private final float capChange;
//    private float requiredProportion;

    public Trade(World world, float cap, float capChange) {
        super(world);
        this.cap = cap;
        this.capChange = capChange / 52;
//        this.requiredProportion = 0.5f;
    }

    @Override
    public int tick() {
        getAgents().forEach(Agent::setUnused);
        distributeCredits();
        float base = calculateCreditBase();
//        float totalPowerThisTick = 0;
//        for (Agent agent: getAgents()) totalPowerThisTick += agent.initialCreditUpdate(base, getDataManager());
        Auction auction = new Auction(this, base, getDataManager());
        auction.commence();
        cap -= capChange;
        return super.tick();
    }

    private void distributeCredits() {
        for (Agent agent: getAgents()) {
            int allowance = (int) ((cap / 52) * agent.getTotalPotential() / getPossibleElectricity());
            agent.addCredits(allowance);
        }
    }

    private float calculateCreditBase() {
        float total = 0;
        for (Agent agent: getAgents()) total += agent.getMeanRequiredCapIncome();
        return total / getAgents().size();
    }

}
