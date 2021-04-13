package tjc.rug.model;

import java.util.ArrayList;
import java.util.Collections;

public class Company {

    private ArrayList<PowerType> holdings;  // The power stations owned by the company
    private ArrayList<Double> tenDayQueue;  // The day by day revenue over the past 10 days
    private double tenDayRevenue;           // The total revenue over the past 10 days
    private double goalPower;               // The goal power production
    private double money;                   // The money held by the company
    private double credits;                 // The number of carbon credits held by the company

    /**
     * Constructor
     * @param money The starting revenue of the company
     */
    public Company(double money) {
        holdings = new ArrayList<>();
        tenDayQueue = new ArrayList<>();
        this.money = money;
    }

    /**
     * Add a power station to the companies holdings
     * @param holding   The power station to add
     */
    public void addHolding(PowerType holding) {
        holdings.add(holding);
    }

    /**
     * Set the goalPower variable based on the production capacity of the company
     * @param portionNeeded     The proportion of possible production capacity to aim for
     */
    public void determineGoalPower(float portionNeeded) {
        for (PowerType holding: holdings) goalPower += holding.getEnergyProduced();
        goalPower *= portionNeeded;
    }

    /**
     * Overloaded method to implement a tick of one time unit. Updates the holdings for the current tax rate
     * then passes to the argument free method.
     * @param tax   The new tax rate
     */
    public void tickTax(float tax) {
        for (PowerType holding: holdings) holding.setCurrentRevenue(tax);
        tickTax();
    }

    /**
     * Overloaded method to implement a tick of one time unit. Picks the cheapest method of generating power.
     */
    public float tickTax() {
        float powerProduced = 0;
        float emissions = 0;
        holdings.sort(new RevenueSorter());
        for (PowerType holding: holdings) {
            powerProduced += holding.getEnergyProduced();
            emissions += holding.getEmissions();
            if (powerProduced >= goalPower) break;
        }
        return emissions;
    }
}
