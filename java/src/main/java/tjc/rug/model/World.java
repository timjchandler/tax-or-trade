package tjc.rug.model;

import tjc.rug.controller.Console;

import java.util.ArrayList;
import java.util.Random;

public class World {

    private static ArrayList<Company> companies;
    private static float taxStart;
    private static float taxEnd;
    private static int endTick;
    private static int ticks;

    public World(int totalTicks, float taxStart, float taxEnd) {
        companies = new ArrayList<>();
        World.taxStart = taxStart;
        World.taxEnd = taxEnd;
        endTick = totalTicks;
        ticks = 0;
        initCompanies(30);
    }

    /**
     * TODO: Make more efficient
     * @param count The number of companies to initialise
     */
    private void initCompanies(int count) {
        Random rand = new Random();
        for (int idx = 0; idx < count; ++idx) companies.add(new Company(0));
        for (int idx = 0; idx < count * 5; ++idx) {
            companies.get(rand.nextInt(count)).addHolding(new PowerType(PowerType.Type.COAL, 1, 2.2f, 0.3f, 0.15f));
            if (idx < count * 3) companies.get(rand.nextInt(count)).addHolding(new PowerType(PowerType.Type.GAS, 1, 0.9f, 0.16f, 0.08f));
            if (idx < count * 2) companies.get(rand.nextInt(count)).addHolding(new PowerType(PowerType.Type.WIND, 1, 0.07f, 0.01f, 0.0f));
        }
        StringBuilder sb = new StringBuilder();
        for (Company company: companies) {
            company.determineGoalPower(0.7f);
            sb.append(company.showHoldings()).append("\n");
        }
        Console.print(sb.toString());
    }

    public static void run() {
        while (ticks < endTick) tick();
    }

    private static void tick() {
        ticks += 1;
        float emissions = 0;
        StringBuilder sb = new StringBuilder();
        for (Company company: companies) {
            emissions += company.tickTax(taxStart + ticks * (taxEnd - taxStart) / endTick);
            sb.append(company.getUsedPlants());
        }
        Console.print(ticks + "\tEmissions: " + emissions + " Plants: " + sb);
    }

}
