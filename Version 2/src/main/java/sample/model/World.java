package sample.model;

import sample.controller.Controller;
import sample.model.agent.Agent;
import sample.model.power.Power;
import sample.model.power.PowerSplit;
import sample.model.power.PowerType;

import java.io.*;
import java.util.ArrayList;

public class World extends Randomiser {

    // The following member variables relate to the setup of the model
    private Controller controller;          // The main controller following the MVC pattern
    private File saveFile;                  // The file in which the results are saved
    private PowerSplit split;               // The split of power types
    private int agentCount;                 // The number of agents
    private final ArrayList<Agent> agents;  // The list of agents
    private boolean isTaxNotTrade = true;   // The type of simulation: true for tax, false for cap and trade
    private float startingMoney = 100;      // TODO: SET THIS PROPERLY
    private int totalTicks = 104;           // The total ticks to run for 104 (2 years) if not set

    // The following member variables relate to the tick updates
    private float taxRate;                      // The current tax rate
    private float taxIncrement;                 // The yearly multiplier to increase/decrease tax
    private float cap;                          // The current cap on carbon emissions
    private float capIncrement;                 // The yearly multiplier to increase/decrease the cap
    private float requiredElectricity = 0;      // The electricity required per tick
    private float electricityIncrement = 1.03f; // The yearly multiplier to increase/decrease the electricity requirement
    private int tick;                           // The current tick
    private static float energyPrice;           // The price money gained from producing electricity

    public World(int seed) {
        this.tick = 0;
        setSeed(seed);
        agents = new ArrayList<>();
        split = new PowerSplit();
        energyPrice = 1;                    // TODO update this
        agentCount = 30;
        setFile();
    }

    public static float getEnergyPrice() {
        return energyPrice;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Generates power plants until a total energy production is met. Assigns these plants to
     * agents randomly
     * @param total The total power to generate
     * @param type  The type of power to generate
     */
    private void setupPower(float total, PowerType type) {
        float set = 0;
        while (set < total) {
            Power power = new Power(type);
            set += power.getProduction();
            agents.get(getInt(agentCount)).addPower(power);
        }
    }

    /**
     * Creates the agents and assigns power plants to them. The split of the power types is based on the split member variable.
     * @param totalEnergy   The total energy to generate between all plants - NOTE: this is the total energy that can be produced, not the energy required per tick
     */
    private void buildWorld(float totalEnergy) {
        createAgents(agentCount);
        setupPower(totalEnergy * split.getCoal(), PowerType.COAL);
        setupPower(totalEnergy * split.getGas(), PowerType.GAS);
        setupPower(totalEnergy * split.getWind(), PowerType.WIND);
        setupPower(totalEnergy * split.getNuclear(), PowerType.NUCLEAR);
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
            Agent agent = new Agent(idx, getNormal(startingMoney), tick);
            addAgent(agent);
        }
    }

    /**
     * Iterate through the agents, updating them for the current tick
     */
    public void updateAgents() {
        for (Agent agent: agents)
            agent.update(tick, taxRate);
    }

    public void saveCSV() {
        try {
            FileWriter fr = new FileWriter(saveFile, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(stateToCSVString());
            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Error: IO exception"); // TODO: More descriptive
        }
    }

    /**
     * Generates a string representing the current state of the model
     * @return  A string listing the state of each agent in the model
     */
    private String stateToCSVString() {
        StringBuilder sb = new StringBuilder();
        if (tick == 0) sb.append("ID,Tick,Electricity,Carbon,Money\n");
        for (Agent agent: agents)
            sb.append(agent.toString()).append("\n");
        return sb.toString();
    }

    /**
     * Increment the tick count and update the agents accordingly. Append the
     * new state to the csv
     */
    public void tick() {
        if (this.tick == 0) saveCSV();
        this.tick++;
        if (this.tick % 52 == 0) yearlyUpdate();
        controller.updateTick(this.tick);
        updateAgents();
        saveCSV();
    }

    private void yearlyUpdate() {
        requiredElectricity *= electricityIncrement;    // TODO: Maybe should be per tick?
        if (isTaxNotTrade) taxRate *= taxIncrement;
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
        setFile();
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
        this.taxRate = taxRate;
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

    public void setElectricityIncrement(float electricityIncrement) {
        this.electricityIncrement = electricityIncrement;
    }

    public void setTotalTicks(int totalTicks) {
        this.totalTicks = totalTicks;
    }

    /**
     * Set the output file for storing the csv
     */
    private void setFile() {
        saveFile = new File("seed-" + getSeed() + ".csv");
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

    // GETTERS ////////////////////////////////////////////////////////////////
    public int getTotalTicks() {
        return totalTicks;
    }

    public float getTaxRate() {
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
}
