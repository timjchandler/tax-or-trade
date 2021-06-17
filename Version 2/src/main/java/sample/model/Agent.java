package sample.model;

import java.util.ArrayList;

public class Agent {

    private final int id;
    private int goal;
    private final ArrayList<Power> power;
    private int tick;
    private int credits;
    private float required;
    private AgentStats stats;

    public Agent(int id, int initialMoney, int tick) {
        this.id = id;
        this.stats = new AgentStats(initialMoney);
        this.tick = tick;
        this.power = new ArrayList<>();
    }

    /**
     * Adds a power plant to the holdings of the agent
     * @param power The power plant to add
     */
    public void addPower(Power power) {
        this.power.add(power);
    }

    /**
     * Updates the agent for the tick
     * Generates power from most profitable plants at current tax rate
     * Updates money and carbon
     * @param tick  The current tick
     * @param tax   The current tax rate
     */
    public void update(int tick, float tax) {
        if (tick == this.tick) return;
        this.tick = tick;
        // TODO: This currently sorts exclusively on income, disregarding the idle costs - could be worth changing
        //       - Note this has been left as is for now
        power.sort((o1, o2) -> Float.compare(o1.calculateIncome(tax), o2.calculateIncome(tax)));
        stats.updateElectricity(0);
        int idx = 0;

        while (stats.getElectricityTick() < required) {
            stats.updateElectricity(power.get(idx).getProduction());
            stats.updateMoney(power.get(idx).calculateIncome(tax));
            stats.updateCarbon(power.get(idx).getCarbon());
            power.get(idx).resetIdle();
            idx++;
        }
        for (; idx < power.size() ; ++idx) {
            power.get(idx).decayIdle();
            stats.updateMoney(-1 * power.get(idx).getIdleCost());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.valueOf(id));
        sb.append(',').append(tick);
        sb.append(',').append(stats.tickToSring());
        return sb.toString();
    }

    public void printPower() {
        StringBuilder sb = new StringBuilder();
        sb.append("-| ID: ").append(id).append("\n");
        for (Power p: power) sb.append(" |- ").append(p.toString()).append("\n");
        System.out.println(sb.toString());
    }

    public int getId() {
        return id;
    }

    public float getMoney() {
        return stats.getMoneyTot();
    }

    public int getTick() {
        return tick;
    }
}
