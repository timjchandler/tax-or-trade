package tjc.model.tick;

import tjc.model.agent.Agent;
import tjc.model.data.DataManager;

/**
 * Operates a decreasing bid auction for the remaining credits of each agent
 */
public class Auction {

    private Trade trade;
    private int totalCredits;
    private float price;
    private DataManager dm;

    public Auction(Trade trade, float initialPrice, DataManager dm) {
        this.trade = trade;
        this.price = initialPrice;
        this.dm = dm;
        totalCredits = 0;
        for (Agent agent: trade.getAgents()) totalCredits += agent.getCredits();
    }

    public void commence() {
//        System.out.println(price);
        while (handleBidding()) price *= 0.99;
//        System.out.println(price);
        trade.getAgents().forEach(Agent::decayIdle);
    }

    private boolean handleBidding() {
        for (Agent agent: trade.getAgents()) {
            int bought = agent.decideBid(price);
            if (bought > 0 && bought >= totalCredits) {
                agent.useCredits(bought, dm);
//                paySellers(bought, agent);
            } else if (bought < totalCredits) return false;
        }
        return true;
    }

    private void paySellers(int credits, Agent buyer) {// TODO: THIS IS VERY VERY SLOW (also doesn't check for credits put in)
        while (credits > 0) {
            for (Agent agent : trade.getAgents()) {
                if (agent.equals(buyer)) continue;
                agent.addCredits(1);
                agent.addMoney(price);
                credits--;
            }
        }
    }


}
