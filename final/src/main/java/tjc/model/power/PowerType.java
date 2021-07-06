package tjc.model.power;

import tjc.model.World;
import tjc.model.tick.Tax;

/**
 * Enum storing types of power plant. Includes:
 * "WIND"   - representative of wind, hydro, and solar power. Assumed to be a farm of roughly 50 turbines or equivalent
 * "GAS"    - representative of natural and derived gas power plants.
 * "COAL"   - representative of coal and lignite power plants - this has been expanded to include oil for simplicity - the difference in generation between the two is negligible when compared to the other types of power.
 * "NUCLEAR"- representative of only nuclear power plants
 * CO2 emissions are measured as tonne / GWh
 */
public enum PowerType {

    WIND        (25, 21.7f, 0f, 6f),//12f),
    GAS         (50, 25.3f,  0f, 245f),//490f),
    COAL        (67, 40.2f, 0f, 420f),//820f),
    NUCLEAR     (150, 32.77f, 0f, 6f);//12f);

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

    /**
     * Getter for the mean power usage
      * @return the mean power usage
     */
    public float getMeanPower() {
        return meanPower;
    }

    /**
     * Getter for the mean cost of operation
     * @return the mean cost of operation
     */
    public float getMeanCost() {
        return meanCost;
    }

    /**
     * Getter for the mean carbon dioxide produced
     * @return The mean CO2 production
     */
    public float getMeanCarbon() {
        return meanCarbon;
    }

    /**
     * Calculate the possible profits based on current tax rate or trade value and energy price
     * @param modifier the current tax rate or trade value as a modifier
     * @return The typical profits for this power type
     */
    public float possibleProfits(float modifier) {
        return (World.getEnergyPrice() - meanCost) / (meanCarbon * modifier);
    }
}
