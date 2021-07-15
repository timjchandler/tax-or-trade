package tjc;

import tjc.model.World;

/**
 * Class that sets up each simulation for multiple runs
 */
public class Runner {

    private int runs;
    private int seed;
    private int ticks;
    private int tax;
    private float trade;
    private String preset;

    /**
     * Constructor sets the parameters for the runs. Begins the runs.
     * @param runs          The total number of runs to complete
     * @param seed          The initial seed (will be incremented by 1 per run)
     * @param ticks         The total number of ticks per run
     * @param tax           The maximum tax rate
     * @param trade         The yearly cap change in percent
     * @param preset        The starting distribution of power plants (calls preset files)
     */
    public Runner(int runs, int seed, int ticks, int tax, float trade, String preset) {
        this.runs = runs;
        this.seed = seed;
        this.ticks = ticks;
        this.tax = tax;
        this.trade = trade;
        this.preset = preset;

        run(true);
        run(false);
    }

    /**
     * Run the simulation according to the stored variables
     * @param isTax True if this is a tax run, False for trade
     */
    private void run(boolean isTax) {
        for (int idx = 0; idx < runs; ++idx) {
            World world = new World(idx + seed);
            world.setTaxOrTrade(isTax);
            world.setTotalTicks(ticks);
            if (isTax) world.setTax(tax);
            else world.setCapIncrement(trade);
            world.setPreset(preset);
            world.start();
        }
    }
}