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
    private File verboseFile;               // The file in which the verbose results are saved
    private PowerSplit split;               // The split of power types
    private int agentCount;                 // The number of agents
    private ArrayList<Power> powerArrayList;// The list of power plants
    private final ArrayList<Agent> agents;  // The list of agents
    private boolean isTaxNotTrade = true;   // The type of simulation: true for tax, false for cap and trade
    private int totalTicks = 1040;           // The total ticks to run for 104 (2 years) if not set

    // The following member variables relate to the tick updates
    private static float taxRate = 10;          // The current tax rate
    private float taxIncrement= 1.2f;           // The yearly multiplier to increase/decrease tax
    private float cap;                          // The current cap on carbon emissions
    private float capIncrement;                 // The yearly multiplier to increase/decrease the cap
    private float requiredElectricity = 7671;   // The electricity required per tick (default is mean EU usage 2018) https://ec.europa.eu/eurostat/statistics-explained/index.php?title=Electricity_production,_consumption_and_market_overview
    private float electricityIncrement = 1.03f; // The yearly multiplier to increase/decrease the electricity requirement
    private int tick;                           // The current tick
    private static float energyPrice = 213.4f;  // The money gained from producing electricity, set as 1000eur per gwh https://ec.europa.eu/eurostat/statistics-explained/index.php?title=Electricity_price_statistics

    public World(int seed) {
        this.tick = 0;
        setSeed(seed);
        agents = new ArrayList<>();
        powerArrayList = new ArrayList<>();
        split = new PowerSplit();
        agentCount = 30;
        setFile("seed-" + getSeed() + ".csv");
        setVerbose("verbose-" + getSeed() + ".csv");
    }

    public static float getEnergyPrice() {
        return energyPrice;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

//    /**
//     * Generates power plants until a total energy production is met. Assigns these plants to
//     * agents randomly
//     * @param total The total power to generate
//     * @param type  The type of power to generate
//     */
//    private float setupPower(float total, PowerType type) {
//        float set = 0;
//        while (set < total) {
//            Power power = new Power(type);
//            set += power.getProduction();
//            agents.get(getInt(agentCount)).addPower(power);
//        }
//        return set;
//    }
    private float setupPower(float total, PowerType type) {
        float set = 0;
        while (set < total) {
            Power power = new Power(type);
            set += power.getProduction();
            powerArrayList.add(power);
            power.setAgent(agents.get(getInt(agentCount)));
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
//        setAgentsRequiredElectricity();
    }

//    private void setAgentsRequiredElectricity() {
//        float totalPossible = 0;
//        for (Agent agent: agents) totalPossible += agent.getTotalPotential();
//        for (Agent agent: agents) agent.setRequired(requiredElectricity * agent.getTotalPotential() / totalPossible);
//    }

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
//
//    /**
//     * Iterate through the agents, updating them for the current tick
//     */
//    public void updateAgents() {
//        for (Agent agent: agents)
//            agent.update(tick, taxRate);
//    }

    public void saveCSV(File file, boolean isVerbose, String string) {
        try {
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            if (isVerbose) br.write(string);
            else br.write(string);
            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Error: IO exception");
        }
    }

    public void saveCSV(File file) {
        saveCSV(file, false, stateToCSVString());
    }

    /**
     * Generates a string representing the current state of the model
     * @return  A string listing the state of each agent in the model
     */
    private String stateToCSVString() {
        StringBuilder sb = new StringBuilder();
        if (tick == 0) sb.append("ID,Tick,Electricity,Carbon,MoneyTick,MoneyTotal\n");
        for (Agent agent: agents)
            sb.append(agent.toString()).append("\n");
        return sb.toString();
    }


    /**
     * Increment the tick count and update the agents accordingly. Append the
     * new state to the csv
     */
//    public void tick() {
//        if (this.tick == 0) {
//            saveCSV(saveFile);
//            saveCSV(verboseFile, true, "ID,Tick,Type,Carbon,Electricity\n");
//        }
//        this.tick++;
//        if (this.tick % 52 == 0) yearlyUpdate();
//        controller.updateTick(this.tick);
//        updateAgents();
//        float energyThisTick = 0;
//        shuffleAgents(agents);
//        for (Agent agent: agents) agent.sortPower(tick, taxRate);
//        while (energyThisTick < requiredElectricity) {
//            for (Agent agent: agents) {
//                // TODO : Make this better to prevent larger power stations being overrepresented
//                String verbose = agent.verbose();
//                if (verbose != null) saveCSV(verboseFile, true, verbose);
//                energyThisTick += agent.updatePower(taxRate);
//                if (energyThisTick > requiredElectricity) break;
//            }
//        }
//        System.out.println("Tick:\t"+ tick + "\tProduced:\t" + energyThisTick + "\tRequired:\t" + requiredElectricity + "\tDifference\t:" + (requiredElectricity - energyThisTick));
//        saveCSV(saveFile);
//    }

    public void tick() {
        if (this.tick == 0) {
            saveCSV(saveFile);
            saveCSV(verboseFile, true, "ID,Tick,Type,Carbon,Electricity\n");
        }
        this.tick++;
        if (this.tick % 52 == 0) yearlyUpdate();
        controller.updateTick(this.tick);
        sortPower();

        float electricityThisTick = 0;
        for (Power power: powerArrayList) {
            if (electricityThisTick > requiredElectricity) break;
            electricityThisTick += power.getProduction();
            power.updateToAgent(tick);
            saveCSV(verboseFile, true, power.verbose());
        }
        for (Agent agent: agents) agent.update(tick);
    }

    private void yearlyUpdate() {
        requiredElectricity *= electricityIncrement;    // TODO: Maybe should be per tick?
        if (isTaxNotTrade) taxRate *= taxIncrement;
        else cap *= capIncrement;
//        setAgentsRequiredElectricity();
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

    private void sortPower() {
        powerArrayList.sort((o1, o2) -> Float.compare(o1.calculateIncome(), o2.calculateIncome()));
    }
}
