package tjc.model.tick;

import tjc.model.agent.Agent;
import tjc.model.data.DataManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Operates a decreasing bid auction for the remaining credits of each agent
 */
public class Auction {

    private Trade trade;
    private long totalCredits;
    private float price;
    private final float initialPrice;
    private DataManager dm;
    private HashMap<Agent, Integer> map;

    public Auction(Trade trade, float initialPrice, DataManager dm) {
        this.trade = trade;
        this.price = this.initialPrice = initialPrice;
        this.dm = dm;
        totalCredits = 0;
        map = new HashMap<>();
        for (Agent agent: trade.getAgents()) {
            totalCredits += agent.getCredits();
            map.put(agent, agent.getCredits());
            agent.zeroCredits();
        }
    }

//    public void start() {
//        float priceDecrease = 0.02f * initialPrice;
//        while (price > 0) {
//            handleBids();
//        }
//    }

//    private void handleBids() {
//        for (Map.Entry<Agent, Integer> entry: map.entrySet()) {
//            if (entry.getKey().acceptPrice(price) > 0) {
//                // TODO: if has credits use own, else buy
//            }
//        }
//    }

    public void commence() {
//        System.out.println(price);
        System.out.println(totalCredits);
        while (handleBidding()) price -= 0.01 * initialPrice;
        trade.getAgents().forEach(Agent::decayIdle);
        reassignCredits();
        System.out.println(totalCredits);
    }

    private boolean handleBidding() {
        for (Agent agent: trade.getAgents()) {
            int bought = agent.decideBid(price);
            if (bought > 0 && bought <= totalCredits) {
                agent.useCredits(bought, dm);
                totalCredits -= bought;
                paySellers(bought, agent);
            } else if (bought > totalCredits || price < 0 ||
                    dm.getElectricityThisTick() > trade.getRequiredElectricity()) return false;
        }
        return true;
    }

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

    private void paySellers(int credits, Agent buyer) {
        paySellers(credits, buyer, map.size() - 1);
    }

    private void reassignCredits() {
        for (Map.Entry<Agent, Integer> entry: map.entrySet())
            entry.getKey().addCredits(entry.getValue());
    }

}
