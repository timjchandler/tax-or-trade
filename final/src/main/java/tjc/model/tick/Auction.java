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
        initialCredits = totalCredits;
    }

    public void commence() {
        while(handleBids() && price > 0) price -= 0.01 * initialPrice;
        reassignCredits();
    }

    private boolean handleBids() {
        for (Map.Entry<Agent, Integer> entry: map.entrySet()) {
            float progress = ((float) totalCredits) / ((float) initialCredits);
            int bought = entry.getKey().decideBid(price, progress);
            if (bought > totalCredits || price == 0 ||
                    dm.getElectricityThisTick() > trade.getRequiredElectricity() * 1.1) return false;
            if (entry.getKey().decideBid(price, progress) > 0) {
                if (entry.getValue() > bought) {
                    entry.setValue(entry.getValue() - bought);
                    entry.getKey().useCredits(bought, dm);

                } else {
                    entry.getKey().addMoney(-1 * bought * price);
                    paySellers(bought, entry.getKey());
                }
                totalCredits -= bought;
            }
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
