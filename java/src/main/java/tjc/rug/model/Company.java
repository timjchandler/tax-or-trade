package tjc.rug.model;

import java.util.ArrayList;

public class Company {

    private ArrayList<PowerType> holdings;  // The power stations owned by the company
    private ArrayList<Double> tenDayQueue;  // The day by day revenue over the past 10 days
    private double tenDayRevenue;           // The total revenue over the past 10 days
    private double goalPower;               // The goal power production
    private double money;                   // The money held by the company
    private double credits;                 // The number of carbon credits held by the company
    private String usedPlants;              // The plants used in the most recent tick

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
    public float tickTax(float tax) {
        for (PowerType holding: holdings) holding.setCurrentRevenue(tax);
        return tickTax();
    }

    /**
     * Overloaded method to implement a tick of one time unit. Picks the cheapest method of generating power.
     * TODO: Oversimplified at present - no incentive not to use wind every time: only shows gas/coal difference
     */
    public float tickTax() {
        float powerProduced = 0;
        float emissions = 0;
        holdings.sort(new RevenueSorter());
        StringBuilder sb = new StringBuilder();
        for (PowerType holding: holdings) {
            powerProduced += holding.getEnergyProduced();
            emissions += holding.getEmissions();
            sb.append(holding.getTypeMinimal());
            if (powerProduced >= goalPower) {
//                System.out.println("Enough power.");
                break;
            }
        }
        usedPlants = sb.toString();
        return emissions;
    }

    public String getUsedPlants() {
        return usedPlants;
    }

    public String showHoldings() {
        StringBuilder sb = new StringBuilder();
        for (PowerType holding: holdings) sb.append(holding.getTypeMinimal());
        return sb.toString();
    }

}
