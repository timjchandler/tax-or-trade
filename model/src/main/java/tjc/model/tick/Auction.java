package tjc.model.tick;

import tjc.model.agent.Agent;
import tjc.model.data.DataManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Operates a decreasing bid auction for the remaining credits of each agent
 */
public class Auction {

    private final Trade trade;
    private final DataManager dm;
    private final HashMap<Agent, Integer> map;
    private final float initialPrice;
    private final long initialCredits;
    private float price;
    private long totalCredits;
    private float totalSpent;

    /**
     * Constructor for the auction. Sets variables and initialises the map. Gathers credits from the agents
     * mapping them to the agent themselves before setting the agents credits to zero.
     * @param trade             The trade object of the current simulation
     * @param initialPrice      The initial bid price
     * @param dm                The data manager for the current simulation
     */
    public Auction(Trade trade, float initialPrice, DataManager dm) {
        this.trade = trade;
        this.price = this.initialPrice = initialPrice;
        this.dm = dm;
        totalCredits = 0;
        totalSpent = 0;
        map = new HashMap<>();
        for (Agent agent: trade.getAgents()) {
            totalCredits += agent.getCredits();
            map.put(agent, agent.getCredits());
            agent.zeroCredits();
        }
        initialCredits = totalCredits;
    }

    /**
     * Begin the auction, receiving bids and decrementing the bid value as required. Reassigns credits once complete.
     */
    public void commence() {
        while(handleBids() && price > 0) price -= 0.01 * initialPrice;
        reassignCredits();
    }

    /**
     * Presents bids to the agents, receives orders from the agents for the number of credits desired when a bid
     * is accepted. Tells the auction to stop once either the credits run out, or the desired electicity total (1.2 *
     * required energy, in line with the over production in real world values) is reached.
     * @return False if the auction should end, else true
     */
    private boolean handleBids() {
        for (Map.Entry<Agent, Integer> entry: map.entrySet()) {
            float progress = ((float) totalCredits) / ((float) initialCredits);
            int bought = entry.getKey().decideBid(price, progress);
            if (bought > totalCredits || price == 0 ||
                    dm.getElectricityThisTick() > trade.getRequiredElectricity() * 1.2) return false;
            if (bought > 0) {
                if (entry.getValue() > bought) {
                    entry.setValue(entry.getValue() - bought);
                    entry.getKey().useCredits(bought, dm);
                } else {
                    entry.getKey().addMoney(-1 * bought * price);
                    paySellers(bought, entry.getKey());
                }
                totalSpent += bought * price;
                totalCredits -= bought;
            }
        }
        return true;
    }

    /**
     * Overloaded method: Recursive part
     * Distributes money to agents based on the amount of their credits sold.
     * @param credits           The number of credits
     * @param buyer             The agent who bought the credits - agents cannot buy from themelves
     * @param remainingSellers  The sellers who still need to be paid
     */
    private void paySellers(int credits, Agent buyer, int remainingSellers) {
        int epsilon = 1000;
        if (remainingSellers == 0 || credits < epsilon) return;
        int creditsPer = credits / remainingSellers;
        int intMargin = credits % remainingSellers;
        int leftover = 0;
        int canSell = 0;
        for (Map.Entry<Agent, Integer> entry: map.entrySet()) {
            if (buyer.equals(entry.getKey())) continue;
            if (entry.getValue() < creditsPer) {
                leftover += creditsPer - entry.getValue();
                entry.getKey().addMoney((creditsPer - entry.getValue()) * price);
                entry.setValue(0);
                continue;
            }
            entry.setValue(entry.getValue() - (creditsPer + intMargin));
            entry.getKey().addMoney((creditsPer + intMargin) * price);
            intMargin = 0;
            canSell++;
        }
        if (leftover != 0) paySellers(leftover, buyer, canSell);
    }

    /**
     * Overloaded method: Recursion initialising part
     * @param credits   The number of credits
     * @param buyer     The agent who bought them
     */
    private void paySellers(int credits, Agent buyer) {
        paySellers(credits, buyer, map.size() - 1);
    }

    /**
     * Reassigns remaining credits to agents.
     */
    private void reassignCredits() {
        for (Map.Entry<Agent, Integer> entry: map.entrySet())
            entry.getKey().addCredits(entry.getValue());
    }

}
