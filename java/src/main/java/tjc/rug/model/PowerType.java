package tjc.rug.model;

public class PowerType {

    public static enum Type {
        WIND, COAL, GAS, NUCLEAR, TIDAL
    }


    private Type type;                  // The type of power production facility
    private float min;
    private float max;
    private float runningCost;          // The cost of running for one time unit
    private float idleCost;             // The base cost of leaving the facility idle per time unit
    private int ticksSinceUse;          // The number of ticks since the facility was used
    private float energyProduced;       // The energy produced per time unit (in monetary value)
    private float carbonProduced;       // The emissions produced per time unit
    private float currentRevenue;       // The revenue that would be returned at the current tax rate


    // https://www.e-education.psu.edu/eme801/node/530 < Shows power building/operating costs

    public PowerType(Type type, float energy, float carbon, float runningCost, float idleCost) {
        this.type = type;
        this.energyProduced = energy;
        this.carbonProduced = carbon;
        this.runningCost = runningCost;
        this.idleCost = idleCost;
    }

    public float getEnergyProduced() {
        return energyProduced;
    }

    public void setCurrentRevenue(float tax) {
        currentRevenue = energyProduced - tax * carbonProduced;
    }

    public float getCurrentRevenue() {
        return currentRevenue;
    }

    public float getEmissions() {
        return carbonProduced;
    }

//    public void setRange() {            // TODO: Find more accurate values and ranges
//        switch (type) {                 // Measured in kg per kwh
//            case COAL:
//                min = 1.0023f;          // https://www.eia.gov/tools/faqs/faq.php?id=74&t=11
//                max = 1.0025f;          // Check against: https://www.rvo.nl/sites/default/files/2013/10/Vreuls%202005%20NL%20Energiedragerlijst%20-%20Update.pdf
//                break;
//            case GAS:
//                min = 0.4126f;
//                max = 0.4128f;
//                break;
//            case WIND:
//                min = 0.01f;
//                max = 0.02f;
//                break;
//            default:
//                min = 0.05f;
//                max = 0.05f;
//        }
//    }

//    public float getMin() {
//        return min;
//    }
//
//    public float getMax() {
//        return max;
//    }
//
//    public static String toString(Type type) {
//        switch (type) {
//            case GAS:  return "Gas";
//            case COAL: return "Coal";
//            case WIND: return "Wind";
//            default: return "Not set yet";
//        }
//    }

//    @Override
//    public String toString() {
//        return toString(type);
////        switch (type) {
////            case GAS:  return "Gas";
////            case COAL: return "Coal";
////            case WIND: return "Wind";
////            default: return "Not set yet";
////        }
//    }

    public String getTypeMinimal() {
        if (type == Type.COAL) return "c";
        if (type == Type.GAS) return "g";
        if (type == Type.WIND) return "w";
        return "u";
    }

}
