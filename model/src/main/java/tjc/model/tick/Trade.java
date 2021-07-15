package tjc.model.tick;

import tjc.model.World;
import tjc.model.agent.Agent;

/**
 * Performs the tick actions for a trade model, inheriting from the AbstractTick class.
 */
public class Trade extends AbstractTick {

    private float cap;                  // The current cap for Carbon Dioxide emissions
    private final float capChange;      // The reduction in the Carbon Dioxide cap per tick
    private static float baseForEstimates;

    public Trade(World world, float capReduction) {
        super(world);
        this.capChange = (float) Math.pow(1 - capReduction, (1f / 52f));
        this.cap = world.getCap();
    }

    /**
     * Iterates a tick. Marks each power plant held by an agent as unused, distributes credits to the
     * agents and initialises an Auction. Calls and returns the super method.
     * @return  The value of the completed tick
     */
    @Override
    public int tick() {
        getAgents().forEach(Agent::setUnused);
        getAgents().forEach(Agent::sortPowerNormalised);
        distributeCredits();
        float base = calculateCreditBase();
        Auction auction = new Auction(this, base, getDataManager());
        auction.commence();
        if (getTick() < 520) cap *= capChange;
        return super.tick();
    }

    /**
     * Distributes the credits to agents based on the potential power generation of each agent with
     * regards to the overall power generation possible.
     */
    private void distributeCredits() {
        getAgents().forEach(Agent::zeroCredits);
        for (Agent agent: getAgents()) {
            int allowance = (int) (cap * agent.getTotalPotential() / getPossibleElectricity());
            agent.addCredits(allowance);
        }
    }

    /**
     * Calculates a base rate for the credits to be valued at
     * @return  The base rate for the credits
     */
    private float calculateCreditBase() {
        float total = 0;
        for (Agent agent: getAgents()) total += agent.getMeanRequiredCapIncome();
        baseForEstimates = total / getAgents().size();
        return baseForEstimates;
    }

    /**
     * Getter for the base rate of value for credits
     * @return The base value for credits
     */
    public static float getBaseForEstimates() {
        return baseForEstimates;
    }

    /**
     * Getter for the current cap
     * @return The current cap
     */
    public float getCap() {
        return cap;
    }
}
