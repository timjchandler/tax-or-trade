package tjc.model.tick;

import tjc.model.World;
import tjc.model.agent.Agent;

/**
 * Performs the tick actions for a trade model, inheriting from the AbstractTick class.
 */
public class Trade extends AbstractTick {

    private float cap;                  // The current cap for Carbon Dioxide emissions
    private final float capChange;      // The reduction in the Carbon Dioxide cap per tick

    /**
     * Constructor sets the cap and change rates as well as storing the world object
     * @param world     the world object
     * @param cap       the initial cap
     * @param capChange the yearly reduction in cap
     */
    public Trade(World world, float cap, float capChange) {
        super(world);
        this.cap = cap;
        this.capChange = capChange / 52;
    }

    /**
     * Iterates a tick. Marks each power plant held by an agent as unused, distributes credits to the
     * agents and initialises an Auction. Calls and returns the super method.
     * @return  The value of the completed tick
     */
    @Override
    public int tick() {
        getAgents().forEach(Agent::setUnused);
        distributeCredits();
        float base = calculateCreditBase();
        Auction auction = new Auction(this, base, getDataManager());
        auction.commence();
        cap -= capChange;
        return super.tick();
    }

    /**
     * Distributes the credits to agents based on the potential power generation of each agent with
     * regards to the overall power generation possible.
     */
    private void distributeCredits() {
        for (Agent agent: getAgents()) {
            int allowance = (int) ((cap / 52) * agent.getTotalPotential() / getPossibleElectricity());
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
        return total / getAgents().size();
    }
}
