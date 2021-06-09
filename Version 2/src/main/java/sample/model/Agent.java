package sample.model;

import java.util.ArrayList;

public class Agent {

    private final int id;
    private int money;
    private float carbon;
    private float electricity;
    private int goal;
    private final ArrayList<Power> power;
    private int tick;
    private int credits;
    private float required;

    public Agent(int id, int initialMoney, int tick) {
        this.id = id;
        this.money = initialMoney;
        this.carbon = 0;
        this.electricity = 0;
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
        power.sort((o1, o2) -> Float.compare(o1.calculateIncome(tax), o2.calculateIncome(tax)));
        electricity = 0;
        int idx = 0;
        // TODO: This currently sorts exclusively on income, disregarding the idle costs - could be worth changing
        while (electricity < required) {
            electricity += power.get(idx).getProduction();
            money += power.get(idx).calculateIncome(tax);
            power.get(idx).resetIdle();
            idx++;
        }
        for (; idx < power.size() ; ++idx) {
            power.get(idx).decayIdle();
            money  -= power.get(idx).getIdleCost();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(',');
        sb.append(money).append(',');
        sb.append(carbon).append(',');
        sb.append(electricity).append(',');
        sb.append("n/a").append(',');       // TODO: Should hold a value or be removed
        sb.append(tick);
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

    public int getMoney() {
        return money;
    }

    public int getTick() {
        return tick;
    }
}
