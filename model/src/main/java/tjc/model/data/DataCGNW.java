package tjc.model.data;

/**
 * Stored and manages the CO2 and electricity values for each type of power plant
 */
public enum DataCGNW {

    /**
     * The reset setting, sets all values to 0
     */
    RESET (0, 0, 0, 0, 0, 0, 0, 0);

    private float coalCarbon;
    private float coalElectricity;
    private float gasCarbon;
    private float gasElectricity;
    private float nuclearCarbon;
    private float nuclearElectricity;
    private float windCarbon;
    private float windElectricity;

    /**
     * Constructor sets the member variables to the input arguments
     * @param coalCarbon        The CO2 from coal
     * @param coalElectricity   The electricity from coal
     * @param gasCarbon         The CO2 from gas
     * @param gasElectricity    The electricity from gas
     * @param nuclearCarbon     The CO2 from nuclear
     * @param nuclearElectricity The electricity from nuclear
     * @param windCarbon        The CO2 from wind
     * @param windElectricity   The electricity from wind
     */
    DataCGNW(float coalCarbon, float coalElectricity, float gasCarbon, float gasElectricity, float nuclearCarbon, float nuclearElectricity, float windCarbon, float windElectricity) {
        this.coalCarbon = coalCarbon;
        this.coalElectricity = coalElectricity;
        this.gasCarbon = gasCarbon;
        this.gasElectricity = gasElectricity;
        this.nuclearCarbon = nuclearCarbon;
        this.nuclearElectricity = nuclearElectricity;
        this.windCarbon = windCarbon;
        this.windElectricity = windElectricity;
    }

    /**
     * Updates the coal values
     * @param carbon        The CO2 to add
     * @param electricity   The elctricity to add
     */
    public void updateCoal(float carbon, float electricity) {
        coalCarbon += carbon;
        coalElectricity += electricity;
    }

    /**
     * Updates the gas values
     * @param carbon        The CO2 to add
     * @param electricity   The electricity to add
     */
    public void updateGas(float carbon, float electricity) {
        gasCarbon += carbon;
        gasElectricity += electricity;
    }

    /**
     * Updates the nuclear values
     * @param carbon        The CO2 to add
     * @param electricity   The electricity to add
     */
    public void updateNuclear(float carbon, float electricity) {
        nuclearCarbon += carbon;
        nuclearElectricity += electricity;
    }

    /**
     * Updates the wind values
     * @param carbon        The CO2 to add
     * @param electricity   The electricity to add
     */
    public void updateWind(float carbon, float electricity) {
        windCarbon += carbon;
        windElectricity += electricity;
    }

    /**
     * Zeroes all stored values
     */
    public void zero() {
        coalCarbon = coalElectricity = 0;
        gasCarbon = gasElectricity = 0;
        nuclearCarbon = nuclearElectricity = 0;
        windCarbon = windElectricity = 0;
    }

    /**
     * Getter for the CO2 from coal
     * @return the CO2 from coal
     */
    public float getCoalCarbon() {
        return coalCarbon;
    }

    /**
     * Getter for the electricity from coal
     * @return the electricity from coal
     */
    public float getCoalElectricity() {
        return coalElectricity;
    }

    /**
     * Getter for the CO2 from gas
     * @return the CO2 from gas
     */
    public float getGasCarbon() {
        return gasCarbon;
    }

    /**
     * Getter for the electricity from gas
     * @return the electricity from gas
     */
    public float getGasElectricity() {
        return gasElectricity;
    }

    /**
     * Getter for the CO2 from nuclear
     * @return the CO2 from nuclear
     */
    public float getNuclearCarbon() {
        return nuclearCarbon;
    }

    /**
     * Getter for the electricity from nuclear
     * @return the electricity from nuclear
     */
    public float getNuclearElectricity() {
        return nuclearElectricity;
    }

    /**
     * Getter for the CO2 from wind
     * @return the CO2 from wind
     */
    public float getWindCarbon() {
        return windCarbon;
    }

    /**
     * Getter for the electricity from wind
     * @return the electricity from wind
     */
    public float getWindElectricity() {
        return windElectricity;
    }

    /**
     * Getter for the sum of all types of electricity generated
     * @return The total sum of electricity generated this tick
     */
    public float getTotalElectricity() {
        return coalElectricity + gasElectricity + nuclearElectricity + windElectricity;
    }

    /**
     * Getter for the sum of all CO2 generated
     * @return The sum of all CO2 generated this tick
     */
    public float getTotalCarbon() {
        return coalCarbon + gasCarbon + nuclearCarbon + windCarbon;
    }
}
