package sample.model.data;

public enum DataCGNW {

    RESET (0, 0, 0, 0, 0, 0, 0, 0);

    private float coalCarbon;
    private float coalElectricity;
    private float gasCarbon;
    private float gasElectricity;
    private float nuclearCarbon;
    private float nuclearElectricity;
    private float windCarbon;
    private float windElectricity;

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

    public void updateCoal(float carbon, float electricity) {
        coalCarbon += carbon;
        coalElectricity += electricity;
    }

    public void updateGas(float carbon, float electricity) {
        gasCarbon += carbon;
        gasElectricity += electricity;
    }

    public void updateNuclear(float carbon, float electricity) {
        nuclearCarbon += carbon;
        nuclearElectricity += electricity;
    }

    public void updateWind(float carbon, float electricity) {
        windCarbon += carbon;
        windElectricity += electricity;
    }

    public float getCoalCarbon() {
        return coalCarbon;
    }

    public void zero() {
        coalCarbon = coalElectricity = 0;
        gasCarbon = gasElectricity = 0;
        nuclearCarbon = nuclearElectricity = 0;
        windCarbon = windElectricity = 0;
    }

    public float getCoalElectricity() {
        return coalElectricity;
    }

    public float getGasCarbon() {
        return gasCarbon;
    }

    public float getGasElectricity() {
        return gasElectricity;
    }

    public float getNuclearCarbon() {
        return nuclearCarbon;
    }

    public float getNuclearElectricity() {
        return nuclearElectricity;
    }

    public float getWindCarbon() {
        return windCarbon;
    }

    public float getWindElectricity() {
        return windElectricity;
    }
}
