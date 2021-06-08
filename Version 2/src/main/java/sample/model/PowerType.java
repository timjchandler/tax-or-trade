package sample.model;

/**
 * Enum storing types of power plant. Includes:
 * "WIND"   - representative of wind, hydro, and solar power.
 * "GAS"    - representative of natural and derived gas power plants.
 * "COAL"   - representative of coal and lignite power plants - this has been expanded to include oil for simplicity - the difference in generation between the two is negligible when compared to the other types of power.
 * "NUCLEAR"- representative of only nuclear power plants
 *
 * CO2 emissions are measured as kg / kWh
 */
public enum PowerType {

    WIND        (1, 0, 0.01f, 0, 0.0f, 0.0f),     // TODO: replace placeholder
    GAS         (2, 0, 0.41f, 0, 0.5f, 0.5f),     // TODO: replace placeholder
    COAL        (3, 0, 1.00f, 0, 0.5f, 1.0f),     // TODO: replace placeholder
    NUCLEAR     (5, 0, 5, 0, 0.6f, 0.1f);     // TODO: replace placeholder

    private final float meanPower;      // The mean power produced by this type
    private final float sdPower;        // The standard deviation of power produced by this type
    private final float meanCost;       // The mean cost of running this plant
    private final float sdCost;         // The standard deviation of the running cost
    private final float upkeepWeight;   // The portion of the running cost needed for upkeep when not running
    private final float meanCarbon;

    /**
     * Constructor
     * @param meanP         The mean power production for this type of plant
     * @param sdP           The standard deviation of power production
     * @param meanC         The mean running cost
     * @param sdC           The standard deviation of the running cost
     * @param upkeepWeight  The proportion of running cost needed to keep the plant idle
     * @param meanCarbon    The mean amount of carbon produced per energy unit
     */
    PowerType(float meanP, float sdP, float meanC, float sdC, float upkeepWeight, float meanCarbon) {
        this.meanPower = meanP;
        this.sdPower = sdP;
        this.meanCost = meanC;
        this.sdCost = sdC;
        this.upkeepWeight = upkeepWeight;
        this.meanCarbon = meanCarbon;
    }

    // Getters

    float getMeanPower() {
        return meanPower;
    }

    float getSdPower() {
        return sdPower;
    }

    float getMeanCost() {
        return meanCost;
    }

    float getSdCost() {
        return sdCost;
    }

    public float getUpkeepWeight() {
        return upkeepWeight;
    }

    public float getMeanCarbon() {
        return meanCarbon;
    }
}
