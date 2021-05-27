package sample.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class World {

    private ArrayList<Agent> agents;
    private float taxRate;
    private float cap;
    private double powerReq;
    private int tick;
    private int seed;
    private static Random rand;
    private File saveFile;

    public World(int seed) {
        this.tick = 0;
        this.seed = seed;
        rand = new Random(seed);
        agents = new ArrayList<>();
        setFile();
    }

    public World() {
        this(new Random().nextInt());
    }

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

    public void addAgent(Agent agent) {
        this.agents.add(agent);
    }

    public void createAgents(int count, int initialMoney) {
        for (int idx = 0; idx < count; ++idx) {
            Agent agent = new Agent(idx, initialMoney, tick);
        }
    }

    public void updateAgents() {
        for (Agent agent: agents)
            agent.update(tick);
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

    public void tick() {
        if (tick == 0) saveCSV();
        this.tick++;
        updateAgents();
        saveCSV();
    }
}
