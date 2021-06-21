package sample.model.power;

/**
 * Enum storing types of power plant. Includes:
 * "WIND"   - representative of wind, hydro, and solar power. Assumed to be a farm of roughly 50 turbines or equivalent
 * "GAS"    - representative of natural and derived gas power plants.
 * "COAL"   - representative of coal and lignite power plants - this has been expanded to include oil for simplicity - the difference in generation between the two is negligible when compared to the other types of power.
 * "NUCLEAR"- representative of only nuclear power plants
 *
 * CO2 emissions are measured as kg / kWh
 */
public enum PowerType {

    // TODO standard deviations and upkeep
    WIND        (5, 0, 2000, 0, 0.5f, 12f),        // TODO: correct cost!!!
    GAS         (50, 0, 1000, 0, 0.5f, 490f),      // TODO: double check meanP
    COAL        (67, 0, 3500, 0, 0.5f, 820f),
    NUCLEAR     (150, 0, 6000, 0, 0.6f, 12f);

    // COSTS: https://www.eia.gov/outlooks/aeo/assumptions/pdf/table_8.2.pdf

    private final float meanPower;      // The mean power produced by this type in Gigawatt Hours (GWH) / week
//    private final float sdPower;        // The standard deviation of power produced by this type
    private final float meanCost;       // The mean cost of running this plant per KW
//    private final float sdCost;         // The standard deviation of the running cost
    private final float upkeepWeight;   // The portion of the running cost needed for upkeep when not running
    private final float meanCarbon;     // The mean CO2 produced as tonnes per GWH

    /**
     * Constructor
     * @param meanP         The mean power production for this type of plant
     * @param sdP           The standard deviation of power production
     * @param meanCost      The mean running cost
     * @param sdC           The standard deviation of the running cost
     * @param upkeepWeight  The proportion of running cost needed to keep the plant idle
     * @param meanCarbon    The mean amount of carbon produced per energy unit
     */
    PowerType(float meanP, float sdP, float meanCost, float sdC, float upkeepWeight, float meanCarbon) {
        this.meanPower = meanP;
//        this.sdPower = sdP;
        this.meanCost = meanCost;
//        this.sdCost = sdC;
        this.upkeepWeight = upkeepWeight;
        this.meanCarbon = meanCarbon;
    }

    // Getters
    float getMeanPower() {
        return meanPower;
    }

//    float getSdPower() {
//        return sdPower;
//    }

    float getMeanCost() {
        return meanCost;
    }

//    float getSdCost() {
//        return sdCost;
//    }

    public float getUpkeepWeight() {
        return upkeepWeight;
    }

    public float getMeanCarbon() {
        return meanCarbon;
    }
}
