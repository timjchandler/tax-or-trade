package tjc.rug.model;

import java.util.Random;

public class Power extends Agent {

    private Random random;
    private PowerType powerType;
    private float carbonPerEnergyUnit;

//    public Power(PowerType powerType) {
//        random = new Random();
//        this.powerType = powerType;
//        initEfficiency();
//    }

//    private void initEfficiency() {
//        float min = powerType.getMin();
//        carbonPerEnergyUnit = random.nextFloat();
//        carbonPerEnergyUnit *= powerType.getMax() - min;
//        carbonPerEnergyUnit += min;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Power type: ");
        sb.append(powerType.toString());
        sb.append("\nProducing: ");
        sb.append(String.format("%.2f", carbonPerEnergyUnit));
        sb.append(" units of energy per unit of carbon.");
        return sb.toString();
    }

}
