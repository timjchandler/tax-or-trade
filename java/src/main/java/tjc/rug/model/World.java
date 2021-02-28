package tjc.rug.model;

import java.util.ArrayList;

public class World {

    private ArrayList<Power> powerPlants;
    private float taxPerUnit;

    public World() {
        powerPlants = new ArrayList<>();
    }

    public void generatePowerPlants(int coal, int gas, int wind, int nuclear, int tidal) {
        genPowerPlantsHelper(PowerType.Type.COAL, coal);
        genPowerPlantsHelper(PowerType.Type.GAS, gas);
        genPowerPlantsHelper(PowerType.Type.WIND, wind);
        genPowerPlantsHelper(PowerType.Type.NUCLEAR, nuclear);
        genPowerPlantsHelper(PowerType.Type.TIDAL, tidal);
    }

    private void genPowerPlantsHelper(PowerType.Type type, int number) {
        for (; number > 0; number--) powerPlants.add(new Power(new PowerType(type)));
    }

}
