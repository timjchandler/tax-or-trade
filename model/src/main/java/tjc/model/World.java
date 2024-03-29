package tjc.model;

import tjc.model.agent.Agent;
import tjc.model.data.DataManager;
import tjc.model.power.Power;
import tjc.model.power.PowerSplit;
import tjc.model.power.PowerType;
import tjc.model.tick.Tax;
import tjc.model.tick.Trade;

import java.util.ArrayList;

/**
 * The world of the model, stores the list of agents the setup and tick variables and the tick managers.
 * Calls the managers to iterate over ticks.
 */
public class World extends Randomiser {

    // The following member variables relate to the setup of the model
    private PowerSplit split;               // The split of power types
    private int agentCount;                 // The number of agents
    private final ArrayList<Agent> agents;  // The list of agents
    private static boolean isTaxNotTrade = false;   // The type of simulation: true for tax, false for cap and trade
    private int totalTicks;           // The total ticks to run for 104 (2 years) if not set
    private String preset;

    // The following member variables relate to the tick updates
    private static float taxRate;           // The current tax rate in 1000Euros per Tonne
    private float taxIncrement;         // The yearly increase to the tax rate
    private static float taxLimit;            // The maximum tax amount
    private float cap;                          // The initial cap on carbon emissions
    private float capIncrement;                 // The yearly amount by which to increase/decrease the cap
    private float requiredElectricity = 5300;   // The electricity required per tick (default is mean EU usage 2018) https://ec.europa.eu/eurostat/statistics-explained/index.php?title=Electricity_production,_consumption_and_market_overview
    private static final float energyPrice = 94.71f;//250f;  // The money gained from producing electricity, set as 1000eur per gwh https://ec.europa.eu/eurostat/statistics-explained/index.php?title=Electricity_price_statistics
    private DataManager dataManager = null;

    // The following are the managers for per tick operations
    private Tax tax;        // The tax manager
    private Trade trade;    // The trade manager

    /**
     * Constructor, sets the seed for the model, initialises the agent list and power split
      * @param seed The seed for the model
     */
    public World(int seed) {
        setSeed(seed);
        agents = new ArrayList<>();
        agentCount = 20;
    }

    /**
     * Creates and distributes the power plants of a given type
     * @param total     The total amount of electricity to be able to generate across all created power plants
     * @param type      The type of power plants to create
     * @return          The amount of electricity all created power plants could generate
     */
    private float setupPower(float total, PowerType type) {
        float set = 0;
        while (set < total) {
            Power power = new Power(type);
            set += power.getProduction();
            cap += power.getCarbon();
            agents.get(getInt(agentCount)).addPower(power);
        }
        return set;
    }

    /**
     * Creates the agents and assigns power plants to them. The split of the power types is based on the split member variable.
     * @param totalEnergy   The total energy to generate between all plants - NOTE: this is the total energy that can be produced, not the energy required per tick
     */
    private void buildWorld(float totalEnergy) {
        dataManager = new DataManager(this);
        cap = 0;
        float totalPotential = 2f;
        totalEnergy *= totalPotential;
        createAgents(agentCount);
        float set = 0;
        set += setupPower(totalEnergy * split.getCoal(), PowerType.COAL);
        set += setupPower(totalEnergy * split.getGas(), PowerType.GAS);
        set += setupPower(totalEnergy * split.getWind(), PowerType.WIND);
        set += setupPower(totalEnergy * split.getNuclear(), PowerType.NUCLEAR);

        cap /= totalPotential;
        float baseMoney = 15 * set * energyPrice / agentCount;
        for (Agent agent: agents) agent.setStartMoney(getNormal(baseMoney));
        setAgentsRequiredElectricity();
        if (isTaxNotTrade) tax = new Tax(this, taxRate, taxIncrement);
        else trade = new Trade(this, capIncrement);
    }

    /**
     * Calculates and sets the required electricity per agent across all agents
     */
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

    /**
     * Iterates over a tick, calling the relevant tick manager for the current model
     * @return  The tick that has been completed
     */
    public int tick()   {
        if (isTaxNotTrade) {
            if (tax.getAgents().size() < 1) {
                System.out.println("Ending run of tax with seed " + getSeed() + " due to lack of agents.");
                return totalTicks;
            }
            return tax.tick();
        }
        else {
            if (trade.getAgents().size() < 1) {
                System.out.println("Ending run of trade with seed " + getSeed() + " due to lack of agents.");
                return totalTicks;
            }
            return trade.tick();
        }
    }

    /**
     * Builds the world using either the values input by the user or the default presets. Loops through as
     * many ticks as have been set
     */
    public void start() {
        buildWorld(requiredElectricity);
        for (int completedTick = tick(); completedTick < totalTicks;) completedTick = tick();
    }

    /**
     * Get the location in which the output CSV is saved
     * @return  The full path of the save location
     */
    public String getSaveLocation() {
        if (dataManager == null) return null;
        return dataManager.getFilepath();
    }

    /**
     * Checks what model is being run
     * @return  True if tax, false if trade
     */
    public boolean isTaxNotTrade() {
        return isTaxNotTrade;
    }

    /**
     * Generates and returns the save file name based on the current settings
     * @return  The filename to save under
     */
    public String getSaveName() {
        StringBuilder dependent = new StringBuilder();
        if (isTaxNotTrade) dependent.append("-tax-").append(formatFloat(taxLimit));
        else dependent.append("-trade-").append(formatFloatTrade(capIncrement));
        return "seed-" + getSeed() + dependent + "-preset-" + preset + ".csv";
    }

    /**
     * Formats floats for the save name. 4 decimal places are shown, with no whole number
     * or decimal point
     * @param f The raw number
     * @return  The formatted number as a string
     */
    private String formatFloat(float f) {
        String out = String.format("%.0f", f * 1000);
        return out;
    }

    /**
     * Formats the floats within the trade model into a string for the save file
     * @param f The float to format
     * @return  The string for the filename
     */
    private String formatFloatTrade(float f) {
        String raw = String.valueOf(f * 100);
        return raw.charAt(0) + "_" + (raw.length() > 2 ? raw.charAt(2) : "");
    }

    // GETTERS
    /**
     * Getter for the price of electricity
     * @return The price in 1000 eur per GWh
     */
    public static float getEnergyPrice() {
        return energyPrice;
    }

    /**
     * Getter for the agents
     * @return The list of agents
     */
    public ArrayList<Agent> getAgents() {
        return agents;
    }

    /**
     * Getter for the number of agents
     * @return The number of agents
     */
    public int getAgentCount() {
        return agentCount;
    }

    /**
     * Getter for the data manager
     * @return The data manager
     */
    public DataManager getDataManager() {
        return dataManager;
    }

    /**
     * Getter for the total ticks to complete
     * @return The maximum ticks for this model run
     */
    public int getTotalTicks() {
        return totalTicks;
    }

    /**
     * Getter for the rate of increase of tax
     * @return The yearly increase in tax
     */
    public float getTaxIncrement() {
        return taxIncrement;
    }

    /**
     * Getter for the CO2 cap
     * @return The maximum emissions of CO2
     */
    public float getCap() {
        return cap;
    }

    /**
     * Getter for the yearly change in the CO2 cap
     * @return The yearly change in CO2 cap
     */
    public float getCapIncrement() {
        return capIncrement;
    }

    /**
     * Getter for the required electricity per tick
     * @return  The total electricity required per tick
     */
    public float getRequiredElectricity() {
        return requiredElectricity;
    }

    // SETTERS
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
        isTaxNotTrade = isTax;
    }

    /**
     * Setter for the tax rate
     * @param taxRate The tax rate to be set
     */
    public void setTaxRate(float taxRate) {
        World.taxRate = taxRate;
    }

    /**
     * Setter for the yearly increase in tax rate
     * @param taxIncrement The yearly increase in tax
     */
    public void setTaxIncrement(float taxIncrement) {
        this.taxIncrement = taxIncrement;
    }

    /**
     * Setter for the cap in CO2 emissions
     * @param cap the cap in CO2
     */
    public void setCap(float cap) {
        this.cap = cap;
    }

    /**
     * Setter for the yearly percentage change in CO2 cap
     * @param capIncrement the yearly change in CO2 cap as a percentage
     */
    public void setCapIncrement(float capIncrement) {
        this.capIncrement = capIncrement / 100;
    }

    /**
     * Setter for the number of ticks to run for
     * @param totalTicks    The number of ticks to run for
     */
    public void setTotalTicks(int totalTicks) {
        this.totalTicks = totalTicks;
    }

    /**
     * Setter for the split in types of power plant
     * @param split The split of power plant types
     */
    public void setSplit(PowerSplit split) {
        this.split = split;
    }

    /**
     * Setter for the preset power split to use
     * @param preset The preset power split to use
     */
    public void setPreset(String preset) {
        this.preset = preset;
        split = new PowerSplit(preset);
    }

    /**
     * Setter for the number of agents to use
     * @param agentCount The number of agents
     */
    public void setAgentCount(int agentCount) {
        this.agentCount = agentCount;
    }

    /**
     * Getter for the tax limit
     * @return The tax limit
     */
    public static float getTaxLimit() {
        return taxLimit;
    }

    /**
     * Getter for the measure currently in use
     * @return True if Tax, false if Trade
     */
    public static boolean isIsTaxNotTrade() {
        return isTaxNotTrade;
    }

    /**
     * Setter for the target tax rate and its constituent variables
     * @param tax The tax rate
     */
    public void setTax(int tax) {
        taxRate = ((float) tax) / 2000f;
        taxLimit = ((float) tax) / 1000f;
        taxIncrement = (taxLimit - taxRate) / 10;
    }
}