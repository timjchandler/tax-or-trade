package sample.model;

import sample.model.agent.Agent;
import sample.model.tick.Trade;
import sample.model.data.DataManager;
import sample.model.power.Power;
import sample.model.power.PowerSplit;
import sample.model.power.PowerType;
import sample.model.tick.Tax;

import java.util.ArrayList;

public class World extends Randomiser {

    // The following member variables relate to the setup of the model
    private PowerSplit split;               // The split of power types
    private int agentCount;                 // The number of agents
    private final ArrayList<Agent> agents;  // The list of agents
    private boolean isTaxNotTrade = true;   // The type of simulation: true for tax, false for cap and trade
    private int totalTicks = 676;           // The total ticks to run for 104 (2 years) if not set
    private String preset = "US-2007";

    // The following member variables relate to the tick updates
    private static float taxRate = 0;           // The current tax rate in 1000Euros per Tonne
    private float taxIncrement= 0.0005f;         // The yearly increase to the tax rate
    private float cap;                          // The current cap on carbon emissions
    private float capIncrement;                 // The yearly multiplier to increase/decrease the cap
    private float requiredElectricity = 7671;   // The electricity required per tick (default is mean EU usage 2018) https://ec.europa.eu/eurostat/statistics-explained/index.php?title=Electricity_production,_consumption_and_market_overview
    private static final float energyPrice = 250f;  // The money gained from producing electricity, set as 1000eur per gwh https://ec.europa.eu/eurostat/statistics-explained/index.php?title=Electricity_price_statistics
    private DataManager dataManager = null;

    private Tax tax;
    private Trade trade;

    public World(int seed) {
        setSeed(seed);
        agents = new ArrayList<>();
        split = new PowerSplit(preset);
        agentCount = 20;
    }

    public static float getEnergyPrice() {
        return energyPrice;
    }

    private float setupPower(float total, PowerType type) {
        float set = 0;
        int count = 0;
        while (set < total) {
            Power power = new Power(type);
            set += power.getProduction();
            agents.get(getInt(agentCount)).addPower(power);
            count++;
        }
        System.out.println(count + "\t" + type.toString());
        return set;
    }

    /**
     * Creates the agents and assigns power plants to them. The split of the power types is based on the split member variable.
     * @param totalEnergy   The total energy to generate between all plants - NOTE: this is the total energy that can be produced, not the energy required per tick
     */
    private void buildWorld(float totalEnergy) {
        dataManager = new DataManager(this);
        totalEnergy *= 2f;
        createAgents(agentCount);
        float set = 0;
        set += setupPower(totalEnergy * split.getCoal(), PowerType.COAL);
        set += setupPower(totalEnergy * split.getGas(), PowerType.GAS);
        set += setupPower(totalEnergy * split.getWind(), PowerType.WIND);
        set += setupPower(totalEnergy * split.getNuclear(), PowerType.NUCLEAR);
        System.out.println("TOTAL: " + totalEnergy + "\tSET: " + set);

        float baseMoney = 15 * set * energyPrice / agentCount;
        for (Agent agent: agents) agent.setStartMoney(getNormal(baseMoney));
        setAgentsRequiredElectricity();
        tax = new Tax(this, taxRate, taxIncrement);
        trade = new Trade(this, cap, capIncrement);
    }

    private void setAgentsRequiredElectricity() {
        float totalPossible = 0;
        for (Agent agent: agents) totalPossible += agent.getTotalPotential();
        for (Agent agent: agents) agent.setRequired(requiredElectricity * agent.getTotalPotential() / totalPossible);
    }

    /**
     * Adds an agent to the array list of agents in the world
     * @param agent The agent to add
     */
    public void addAgent(Agent agent) {
        this.agents.add(agent);
    }

    /**
     * Creates a number of agents equal to the count argument providing them each
     * with an initialMoney amount of money. This money will then be updated based
     * on the holdings of the agent
     * @param count The number of agents to create
     */
    public void createAgents(int count) {
        for (int idx = 0; idx < count; ++idx) {
            Agent agent = new Agent(idx, 0);
            addAgent(agent);
        }
    }

    public int tick() {
        if (isTaxNotTrade) return tax.tick();
        else return trade.tick();
    }

    /**
     * Builds the world using either the values input by the user or the default presets. Loops through as
     * many ticks as have been set
     */
    public void start() {
        buildWorld(requiredElectricity);
        int totalPlants = 0;
        for (Agent agent: agents) totalPlants += agent.getPower().size();
        System.out.println(":: " + agents.size() + " agents, " + totalPlants + " power plants");
        for (int completedTick = tick(); completedTick < totalTicks;) completedTick = tick();
    }

    // SETTERS ////////////////////////////////////////////////////////////////
    /**
     * Updates the seed in Randomiser, updates the save file name
     * @param seed  The new seed to be set
     */
    public void updateSeed(int seed) {
        setSeed(seed);
    }

    /**
     * Sets the amount of electricity required in a given tick
     * @param electricity the amount of electricity required
     */
    public void setRequiredElectricity(float electricity) {
        this.requiredElectricity = electricity;
    }

    /**
     * Changes the type of simulation between tax and trade
     * @param isTax True if a tax simulation, false if cap and trade
     */
    public void setTaxOrTrade(boolean isTax) {
        this.isTaxNotTrade = isTax;
    }

    public void setTaxRate(float taxRate) {
        World.taxRate = taxRate;
    }

    public void setTaxIncrement(float taxIncrement) {
        this.taxIncrement = taxIncrement;
    }

    public void setCap(float cap) {
        this.cap = cap;
    }

    public void setCapIncrement(float capIncrement) {
        this.capIncrement = capIncrement;
    }

    public void setTotalTicks(int totalTicks) {
        this.totalTicks = totalTicks;
    }

    public void setSplit(PowerSplit split) {
        this.split = split;
    }

    public void setPreset(String preset) {
        this.preset = preset;
    }

    public void setAgentCount(int agentCount) {
        this.agentCount = agentCount;
    }

    // GETTERS ////////////////////////////////////////////////////////////////
    public int getTotalTicks() {
        return totalTicks;
    }

    public float getTaxIncrement() {
        return taxIncrement;
    }

    public float getCap() {
        return cap;
    }

    public float getCapIncrement() {
        return capIncrement;
    }

    public float getRequiredElectricity() {
        return requiredElectricity;
    }

    /**
     * Get the location in which the output CSV is saved
     * @return  The full path of the save location
     */
    public String getSaveLocation() {
        if (dataManager == null) return null;
        return dataManager.getFilepath();
    }

    public boolean isTaxNotTrade() {
        return isTaxNotTrade;
    }

    public String getSaveName() {
        StringBuilder dependent = new StringBuilder();
        if (isTaxNotTrade) {
            dependent.append("-tax-").append(formatFloat(taxRate));
            dependent.append("-inc-").append(formatFloat(taxIncrement));
        } else {
            dependent.append("-trade").append(formatFloat(cap));
            dependent.append("-inc-").append(formatFloat(capIncrement));
        }
        return "seed-" + getSeed() +
                dependent +
                "-preset-" + preset +
                ".csv";
    }

    private String formatFloat(float f) {
        String out = String.format("%.4f", f);
        return out.substring(2);
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public int getAgentCount() {
        return agentCount;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}