package sample.model;

import sample.controller.Controller;
import sample.model.agent.Agent;
import sample.model.data.DataManager;
import sample.model.power.Power;
import sample.model.power.PowerSplit;
import sample.model.power.PowerType;

import java.io.*;
import java.util.ArrayList;

public class World extends Randomiser {

    // The following member variables relate to the setup of the model
    private Controller controller;          // The main controller following the MVC pattern
    private File saveFile;                  // The file in which the results are saved
    private File verboseFile;               // The file in which the verbose results are saved
    private PowerSplit split;               // The split of power types
    private int agentCount;                 // The number of agents
    private final ArrayList<Agent> agents;  // The list of agents
    private boolean isTaxNotTrade = true;   // The type of simulation: true for tax, false for cap and trade
    private int totalTicks = 1040;           // The total ticks to run for 104 (2 years) if not set
    private String preset = "US-2007";

    // The following member variables relate to the tick updates
    private static float taxRate = 0;           // The current tax rate in 1000Euros per Tonne
    private float taxIncrement= 0.005f;         // The yearly increase to the tax rate
    private float cap;                          // The current cap on carbon emissions
    private float capIncrement;                 // The yearly multiplier to increase/decrease the cap
    private float requiredElectricity = 7671;   // The electricity required per tick (default is mean EU usage 2018) https://ec.europa.eu/eurostat/statistics-explained/index.php?title=Electricity_production,_consumption_and_market_overview
    private final float electricityIncrement = 1.00057f; // The weekly multiplier to increase/decrease the electricity requirement
    private int tick;                           // The current tick
    private static float energyPrice = 213.4f;  // The money gained from producing electricity, set as 1000eur per gwh https://ec.europa.eu/eurostat/statistics-explained/index.php?title=Electricity_price_statistics
    private float newBuildChance = 0.1f;
    private DataManager dataManager;

    public World(int seed) {
        this.tick = 0;
        setSeed(seed);
        agents = new ArrayList<>();
        split = new PowerSplit(preset);
        agentCount = 30;
        setFile("seed-" + getSeed() + "-" + preset + ".csv");
        setVerbose("verbose-" + getSeed() + "-" + preset + ".csv");
    }

    public static float getEnergyPrice() {
        return energyPrice;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private float setupPower(float total, PowerType type) {
        float set = 0;
        while (set < total) {
            Power power = new Power(type);
            set += power.getProduction();
            agents.get(getInt(agentCount)).addPower(power);
        }
        return set;
    }

    /**
     * Creates the agents and assigns power plants to them. The split of the power types is based on the split member variable.
     * @param totalEnergy   The total energy to generate between all plants - NOTE: this is the total energy that can be produced, not the energy required per tick
     */
    private void buildWorld(float totalEnergy) {
        totalEnergy *= 1.5f;
        createAgents(agentCount);
        float set = 0;
        set += setupPower(totalEnergy * split.getCoal(), PowerType.COAL);
        set += setupPower(totalEnergy * split.getGas(), PowerType.GAS);
        set += setupPower(totalEnergy * split.getWind(), PowerType.WIND);
        set += setupPower(totalEnergy * split.getNuclear(), PowerType.NUCLEAR);
        System.out.println("TOTAL: " + totalEnergy + "\tSET: " + set);

        float baseMoney = 5 * set * energyPrice / agentCount;
        for (Agent agent: agents) agent.setStartMoney(getNormal(baseMoney));
        setAgentsRequiredElectricity();
        dataManager = new DataManager(this);
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
            Agent agent = new Agent(idx, tick);
            addAgent(agent);
        }
    }

    public void tick() {
        this.tick++;
        if (this.tick % 4 == 0) monthlyUpdate();
        controller.updateTick(this.tick);
        for (Agent agent: agents) agent.updateData(tick, dataManager);
        if (getInt(100) < 100 * newBuildChance)
            agents.get(getInt(agentCount)).addPower(new Power(chooseNewPower()));
        dataManager.write(tick);
        requiredElectricity *= electricityIncrement;
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

    private void monthlyUpdate() {
        float total = 0;
        for (Agent agent: agents) total += agent.getTotalPotential();
        for (Agent agent: agents) agent.setRequired(requiredElectricity * agent.getTotalPotential() / total);
        if (this.tick % 52 == 0) yearlyUpdate();
    }

    private void yearlyUpdate() {
        if (isTaxNotTrade) taxRate += taxIncrement;
        else cap *= capIncrement;
    }

    /**
     * Builds the world using either the values input by the user or the default presets. Loops through as
     * many ticks as have been set
     */
    public void start() {
        buildWorld(requiredElectricity > 0 ? requiredElectricity : 100);    // TODO actual value
        while (tick < totalTicks) tick();
    }

    // SETTERS ////////////////////////////////////////////////////////////////
    /**
     * Updates the seed in Randomiser, updates the save file name
     * @param seed  The new seed to be set
     */
    public void updateSeed(int seed) {
        setSeed(seed);
        setFile("seed-" + getSeed() + ".csv");
        setVerbose("verbose-" + getSeed() + ".csv");
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

    /**
     * Set the output file for storing the csv
     */
    private void setFile(String name) {
        saveFile = new File(name);
        if(saveFile.exists()){
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
            System.out.println(":: New save file - " + saveFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setVerbose(String name) {
        verboseFile = new File(name);
        if (verboseFile.exists()) verboseFile.delete();
        try {
            verboseFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GETTERS ////////////////////////////////////////////////////////////////
    public int getTotalTicks() {
        return totalTicks;
    }

    public static float getTaxRate() {
        return taxRate;
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
        return saveFile.getAbsolutePath();
    }

    public boolean isTaxNotTrade() {
        return isTaxNotTrade;
    }

    public String getSaveName() {
        return "seed-" + getSeed() +
                "-type-" + (isTaxNotTrade ? "tax" : "trade") +
                "-preset-" + preset +
                ".csv";
    }
}
