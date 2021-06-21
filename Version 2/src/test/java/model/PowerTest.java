package model;

import org.junit.Assert;
import org.junit.Test;
import sample.model.power.Power;
import sample.model.power.PowerType;

public class PowerTest {

    /**
     * Test the power object, assert that type is correctly set then create a sample object for each
     * type and print member variables
     */
    @Test
    public void testPower() {
        Assert.assertEquals(PowerType.GAS, new Power(PowerType.GAS).getType());
        testPowerHelper(PowerType.GAS);
        testPowerHelper(PowerType.COAL);
        testPowerHelper(PowerType.WIND);
        testPowerHelper(PowerType.NUCLEAR);
    }

    /**
     * Called by testPower. Creates new object and prints member variables
     * @param type  The power type to use for the new Power object
     */
    public void testPowerHelper(PowerType type) {
        Power power = new Power(type);
        System.out.println("Type:\t\t\t" + power.getType());
        System.out.println("Production:\t\t" + power.getProduction());
        System.out.println("Running Cost:\t" + power.getRunningCost());
        System.out.println("Idle Cost:\t\t" + power.getIdleCost());
    }

    /**
     * Tests the decay of the idle cost
     */
    @Test
    public void testDecay() {
        Power power = new Power(PowerType.GAS);
        for (int idx = 0; idx < 10; ++idx) {
            System.out.println(power.getIdleCost());
            power.decayIdle();
        }
    }

}
