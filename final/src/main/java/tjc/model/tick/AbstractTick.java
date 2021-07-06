package tjc.model.tick;

import tjc.model.Randomiser;
import tjc.model.World;
import tjc.model.agent.Agent;
import tjc.model.data.DataManager;
import tjc.model.power.Power;
import tjc.model.power.PowerType;

import java.util.ArrayList;


/**
 * Abstract class to be extended by the tax and trade classes. Manages generic tick based operations and stores
 * variables relevant to both approaches. Extends the randomiser for random operations such as building new power
 * plants.
 */
public abstract class AbstractTick extends Randomiser {

    private final World world;                                  // The world of the model
    private final ArrayList<Agent> agents;                      // The agents in the model
    private final DataManager dataManager;                      // Records and saves the carbon/electricity production
    private int agentCount;                                     // The current number of agents
    private int tick;                                           // The current tick
    private float requiredElectricity;                          // The electricity that must be generated per tick
    private final float electricityIncrement = 1 + 0.03f / 52;  // Electricity increment at 3%/year. Applied per week
    private final float newBuildChance = 0.2f;                  // The per tick probability of building a new power plant
    private float possibleElectricity;                          // The maximum electricity that can be generated

    /**
     * Abstrace constructor. Sets the world variable and all derived variables. Initialises the tick counter to 0.
     * Initialises and calculates the total possible electricity generation accross all agents.
     * @param world The world of the simulation
     */
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

    /**
     * Iterates over one tick - representing one week. Updates the tick counter, removes bankrupt agents
     * updates the electricity requirement and the individual electricity requirements per agent. Calculates
     * whether a new power plant will be built and if so assigns it to an agent. Writes data to the data manager
     * @return  The tick that has just been completed
     */
    public int tick() {
        this.tick++;
        cleanAgents();
        requiredElectricity *= electricityIncrement;
        setAgentsRequiredElectricity();
        if (getInt(100) < 100 * newBuildChance)
            agents.get(getInt(agentCount)).addPower(new Power(chooseNewPower()));
        dataManager.write(tick);
        agents.forEach(Agent::costUnused);
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
        for (Power power: toRedistribute) if (power.getIdleTime() == 0) agents.get(getInt(agentCount)).addPower(power);
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
        float modifier = World.isIsTaxNotTrade() ? Tax.getTaxRate() : Trade.getBaseForEstimates();
        float coal = (float) Math.pow(PowerType.COAL.possibleProfits(modifier), 2) / PowerType.COAL.getMeanPower();
        float gas = (float) Math.pow(PowerType.GAS.possibleProfits(modifier), 2) / PowerType.GAS.getMeanPower();
        float nuclear = (float) Math.pow(PowerType.NUCLEAR.possibleProfits(modifier), 2) / PowerType.NUCLEAR.getMeanPower();
        float wind = (float) Math.pow(PowerType.WIND.possibleProfits(modifier), 2) / PowerType.WIND.getMeanPower();
        float total = coal + gas + nuclear + wind;
        int choice = getInt(100);
        if (choice < 100 * coal / total) return PowerType.COAL;
        if (choice < 100 * (coal + gas) / total) return PowerType.GAS;
        if (choice < 100 * (coal + gas + nuclear) / total) return PowerType.NUCLEAR;
        return PowerType.WIND;
    }

    /**
     * Getter for the world
     * @return The world of the simulation
     */
    public World getWorld() {
        return world;
    }

    /**
     * Getter for the current tick
     * @return The current tick of the model
     */
    public int getTick() {
        return tick;
    }

    /**
     * Getter for the data manager
     * @return The data manager recording this iteration of the model
     */
    public DataManager getDataManager() {
        return dataManager;
    }

    /**
     * Getter for the current agent list
     * @return An array list containing all active agents
     */
    public ArrayList<Agent> getAgents() {
        return agents;
    }

    /**
     * Getter for the maximum possible electricity generation per tick
     * @return The current maximum possible electricity in GWh
     */
    public float getPossibleElectricity() {
        return possibleElectricity;
    }

    /**
     * Getter for the amount of electricity required per tick
     * @return The amount of electricity required per tick
     */
    public float getRequiredElectricity() {
        return requiredElectricity;
    }
}
