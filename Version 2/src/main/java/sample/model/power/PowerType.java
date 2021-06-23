package sample.model.power;

import sample.model.Tax;
import sample.model.World;

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
    WIND        (25, 45, 0f, 12f),
    GAS         (50, 37,  0f, 490f),
    COAL        (67, 99, 0f, 820f),
    NUCLEAR     (150, 63, 0f, 12f);

    // COSTS: https://www.eia.gov/outlooks/aeo/assumptions/pdf/table_8.2.pdf < this is costs for new
    // COSTS 2: https://www.iea.org/reports/projected-costs-of-generating-electricity-2020

    private final float meanPower;      // The mean power produced by this type in Gigawatt Hours (GWH) / week
    private final float meanCost;       // The mean cost of running this plant as 1000EUR/GWH
    private final float upkeepWeight;   // The portion of the running cost needed for upkeep when not running
    private final float meanCarbon;     // The mean CO2 produced as tonnes per GWH

    /**
     * Constructor
     * @param meanP         The mean power production for this type of plant
     * @param meanCost      The mean running cost
     * @param upkeepWeight  The proportion of running cost needed to keep the plant idle
     * @param meanCarbon    The mean amount of carbon produced per energy unit
     */
    PowerType(float meanP, float meanCost, float upkeepWeight, float meanCarbon) {
        this.meanPower = meanP;
        this.meanCost = meanCost;
        this.upkeepWeight = upkeepWeight;
        this.meanCarbon = meanCarbon;
    }

    // Getters
    public float getMeanPower() {
        return meanPower;
    }

    public float getMeanCost() {
        return meanCost;
    }

    public float getUpkeepWeight() {
        return upkeepWeight;
    }

    public float getMeanCarbon() {
        return meanCarbon;
    }

    public float possibleProfits() {
        return World.getEnergyPrice() - (meanCarbon * Tax.getTaxRate() + upkeepWeight);
    }
}
