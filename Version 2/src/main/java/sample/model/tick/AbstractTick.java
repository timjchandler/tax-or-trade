package sample.model.tick;

import sample.model.Randomiser;
import sample.model.World;
import sample.model.agent.Agent;
import sample.model.data.DataManager;
import sample.model.power.Power;
import sample.model.power.PowerType;

import java.util.ArrayList;

public abstract class AbstractTick extends Randomiser {

    private final World world;
    private final ArrayList<Agent> agents;
    private final DataManager dataManager;
    private int agentCount;
    private int tick;
    private float requiredElectricity;
    private final float electricityIncrement = 1 + 0.03f / 52;
    private final float newBuildChance = 0.4f;
    private float possibleElectricity;

    public AbstractTick(World world) {
        this.world = world;
        this.agents = (ArrayList<Agent>) world.getAgents().clone();
        this.agentCount = world.getAgentCount();
        this.requiredElectricity = world.getRequiredElectricity();
        this.dataManager = world.getDataManager();
        this.tick = 0;
        this.possibleElectricity = 0;
        for (Agent agent: agents) possibleElectricity += agent.getTotalPotential();
    }

    public int tick() {
        this.tick++;
//        world.getController().updateTick(tick); // TODO commented whilst running without gui
        cleanAgents();
        requiredElectricity *= electricityIncrement;
        setAgentsRequiredElectricity();
        if (getInt(100) < 100 * newBuildChance)
            agents.get(getInt(agentCount)).addPower(new Power(chooseNewPower()));
        dataManager.write(tick);
        return tick;
    }

    /**
     * Removes agents that have less than 0 remaining money. Redistributes
     * the power plants of the removed agents amongst the remaining agents
     */
    public void cleanAgents() {
        ArrayList<Agent> toRemove = new ArrayList<>();
        ArrayList<Power> toRedistribute = new ArrayList<>();
        for (Agent agent: agents) {
            if (agent.getMoneyTot() < 0) {
                toRemove.add(agent);
                toRedistribute.addAll(agent.getPower());
                agentCount--;
            }
        }
        for (Agent agent: toRemove) agents.remove(agent);
        for (Power power: toRedistribute) agents.get(getInt(agentCount)).addPower(power);
    }

    /**
     * Updates the electricity goal of agents based on the potential electricity generation
     * of the agents relative to other agents.
     */
    private void setAgentsRequiredElectricity() {
        float totalPossible = 0;
        for (Agent agent: agents) totalPossible += agent.getTotalPotential();
        for (Agent agent: agents) agent.setRequired(requiredElectricity * agent.getTotalPotential() / totalPossible);
    }

    /**
     * Selects a power type to build.
     * Picks the type based on the relative size of the following:
     *      Possible profits squared:   This represents the economic desirability of the type of power plant
     *      1 / mean power generation:  This represents the size, and ease of building the type of power plant
     * @return The type of power plant to build
     */
    private PowerType chooseNewPower() {
        float coal = (float) Math.pow(PowerType.COAL.possibleProfits(), 2) / PowerType.COAL.getMeanPower();
        float gas = (float) Math.pow(PowerType.GAS.possibleProfits(), 2) / PowerType.GAS.getMeanPower();
        float nuclear = (float) Math.pow(PowerType.NUCLEAR.possibleProfits(), 2) / PowerType.NUCLEAR.getMeanPower();
        float wind = (float) Math.pow(PowerType.WIND.possibleProfits(), 2) / PowerType.WIND.getMeanPower();
        float total = coal + gas + nuclear + wind;
        int choice = getInt(100);
        if (choice < 100 * coal / total) return PowerType.COAL;
        if (choice < 100 * (coal + gas) / total) return PowerType.GAS;
        if (choice < 100 * (coal + gas + nuclear) / total) return PowerType.NUCLEAR;
        return PowerType.WIND;
    }

    public World getWorld() {
        return world;
    }

    public int getTick() {
        return tick;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public float getBuildChance() {
        return newBuildChance;
    }

    public int getAgentCount() {
        return agentCount;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public float getPossibleElectricity() {
        return possibleElectricity;
    }
}
