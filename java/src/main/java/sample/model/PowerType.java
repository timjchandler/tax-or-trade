package sample.model;

public class PowerType {

    public static enum Type {
        WIND, COAL, GAS, NUCLEAR, TIDAL
    }

    private Type type;
    private float min;
    private float max;

    public PowerType(Type type) {
        this.type = type;
        setRange();
    }

    public void setRange() {            // TODO: Find accurate values
        switch (type) {
            case COAL:
                min = 1f;
                max = 2f;
                break;
            case GAS:
                min = 0.5f;
                max = 1f;
                break;
            case WIND:
                min = 0.01f;
                max = 0.02f;
                break;
            default:
                min = 0.05f;
                max = 0.05f;
        }
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    @Override
    public String toString() {
        switch (type) {
            case GAS:  return "Gas";
            case COAL: return "Coal";
            case WIND: return "Wind";
            default: return "Not set yet";
        }
    }
}
