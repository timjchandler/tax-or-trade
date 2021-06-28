package tjc.model.tick;

import tjc.model.World;
import tjc.model.agent.Agent;

public class Trade extends AbstractTick {

    private float cap;
    private float capChange;
    private float requiredProportion;

    public Trade(World world, float cap, float capChange) {
        super(world);
        this.cap = cap;
        this.capChange = capChange;
        this.requiredProportion = 0.5f;
    }

    @Override
    public int tick() {
        getAgents().forEach(Agent::setUnused);
        distributeCredits();
        float base = calculateCreditBase();
        float totalPowerThisTick = 0;
        for (Agent agent: getAgents()) totalPowerThisTick += agent.initialCreditUpdate(base, getDataManager());
        System.out.println(totalPowerThisTick + "/" + getRequiredElectricity());
        Auction auction = new Auction(this, base, getDataManager());
        auction.commence();
        cap -= capChange / 52;
        return super.tick();
    }

    private void distributeCredits() {
        for (Agent agent: getAgents()) {
            int allowance = (int) ((cap / 52) * agent.getTotalPotential() / getPossibleElectricity());
            agent.addCredits(allowance);
        }
    }

    private void zeroCredits() {
        for (Agent agent: getAgents()) agent.zeroCredits();
    }

    private float calculateBaseCreditPrice() {
        float totalValue = 0;
        int count = 0;
        for (Agent agent: getAgents()) {
            totalValue += agent.getMeanCarbonValue();
            count += agent.getPower().size();
        }
        return totalValue / count;
    }

    private float calculateCreditBase() {
        float total = 0;
        for (Agent agent: getAgents()) total += agent.getMeanRequiredCapIncome();
        return total / getAgents().size();
    }

    /**
     * Getter for the proportion of the required production of an agent that must always be completed
     * per tick
     * @return  The required proportion
     */
    public float getRequiredProportion() {
        return requiredProportion;
    }

    /**
     * Setter for the proportion of an agents required production that must always be completed
     * per tick
     * @param requiredProportion The proportion of an agents power that must be produced
     */
    public void setRequiredProportion(float requiredProportion) {
        this.requiredProportion = requiredProportion;
    }
}
