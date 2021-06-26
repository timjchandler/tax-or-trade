package sample.model.agent;

import sample.model.data.DataManager;
import sample.model.power.Power;

import java.util.ArrayList;
import java.util.Collections;

public class Agent {

    private final int id;
    private final ArrayList<Power> power;
    private int tick;
    private int credits;
    private float required;
    private final AgentStats stats;

    public Agent(int id, int tick) {
        this.id = id;
        this.stats = new AgentStats();
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

    public void setStartMoney(float initialMoney) {
        stats.setMoneyTot(initialMoney);
    }

    public void setRequired(float required) {
        this.required = required;
    }

    public int getId() {
        return id;
    }

    public float getTotalPotential() {
        float total = 0;
        for (Power p: power) total += p.getProduction();
        return total;
    }

    public void updateDataTax(int tick, DataManager dataManager) {
        if (this.tick == tick) System.out.println(":: WARNING: Agent " + id + " called twice in tick " + tick);
        this.tick = tick;
        power.sort(((o1, o2) -> Float.compare(o1.normalisedIncomeFromTax(), o2.normalisedIncomeFromTax())));
        Collections.reverse(power);
        float totalElecticity = 0;
        ArrayList<Power> toRemove = new ArrayList<>();

        for(Power p: power) {
            if (totalElecticity > required) {
                if (!p.decayIdle()) toRemove.add(p);
            }
            else totalElecticity += usePower(p, dataManager);
        }
        for (Power p: toRemove) deletePower(p);
    }

    public void updateDataTrade(int tick, DataManager dataManager) {
        if (this.tick == tick) System.out.println(":: WARNING: Agent " + id + " called twice in tick " + tick);
        this.tick = tick;
        power.sort(((o1, o2) -> Float.compare(o1.carbonNormalisedIncome(), o2.carbonNormalisedIncome())));
        Collections.reverse(power);
        float totalElectricity = 0;
        ArrayList<Power> toRemove = new ArrayList<>();

        for(Power p: power) {
            p.clearUse();//TODO ADD MORE TO THIS FUNCTION
            if (totalElectricity > required) if (!p.decayIdle()) toRemove.add(p);

        }
        for (Power p: toRemove) deletePower(p);
    }

    private float usePower(Power p, DataManager dm) {
        stats.updateElectricity(p.getProduction(), tick);
        stats.updateMoney(p.calculateIncomeFromTax());
        stats.updateCarbon(p.getCarbon());
        dm.add(p);
        return p.getProduction();
    }

    private void deletePower(Power p) {
        power.remove(p);
    }

    public ArrayList<Power> getPower() {
        return power;
    }

    public double getMoneyTot() {
        return stats.getMoneyTot();
    }

    public void addCredits(int credits) {
        this.credits += credits;
    }

    public void zeroCredits() {
        this.credits = 0;
    }

    public float getMeanCarbonValue() {
        float total = 0;
        for (Power p: power) total += p.carbonNormalisedIncome();
        return total / power.size();
    }
}
