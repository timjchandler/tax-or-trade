package sample.model;

import sample.controller.Controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class World extends Randomiser {

    private final ArrayList<Agent> agents;
    private float taxRate;
    private float cap;
    private double powerReq;
    private int tick;
    private static int seed;
    private File saveFile;
    private PowerSplit split;
    private int startingMoney;
    private int agentCount;
    private static float energyPrice;
    private Controller controller;

    public World(int seed) {
        this.tick = 0;
        this.seed = seed;
        setSeed(seed);
        agents = new ArrayList<>();
        split = new PowerSplit();
        energyPrice = 1;                    // TODO update this
        agentCount = 30;
        setFile();
    }

    public World() {
        this(new Random().nextInt());
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
            Agent agent = new Agent(idx, startingMoney, tick);
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
     * Set the output file for storing the csv
     */
    private void setFile() {
        saveFile = new File("seed-" + seed + ".csv");
        if(saveFile.exists()){
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a string representing the current state of the model
     * @return  A string listing the state of each agent in the model
     */
    private String stateToCSVString() {
        StringBuilder sb = new StringBuilder();
        if (tick == 0) sb.append("ID,Money,Carbon,Electricity,Type,Tick\n");
        for (Agent agent: agents)
            sb.append(agent.toString()).append("\n");
        return sb.toString();
    }

    public int getTick() {
        return tick;
    }

    /**
     * Increment the tick count and update the agents accordingly. Append the
     * new state to the csv
     */
    public void tick() {
        if (tick == 0) saveCSV();
        this.tick++;
        controller.updateTick(tick);
        updateAgents();
        saveCSV();
    }

    public String getSaveLocation() {
        return saveFile.getAbsolutePath();
    }

    public Controller getController() {
        return controller;
    }

    public void start() {
        System.out.println("::WORLD:: Start");
        buildWorld(100);    // TODO actual value
        tick();
    }

    @Override
    public static void setSeed(int seed) {

    }
}
