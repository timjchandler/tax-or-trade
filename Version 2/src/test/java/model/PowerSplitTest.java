//package model;
//
//import org.junit.Assert;
//import org.junit.Test;
//import sample.model.power.PowerSplit;
//
//public class PowerSplitTest {
//
//    private final String defaultOut = "Gas:\t\t0.25\nCoal:\t\t0.25\nWind:\t\t0.25\nNuclear:\t0.25\nTOTAL:\t\t10000";
//    private final String us_2020Out = "Gas:\t\t0.406\nCoal:\t\t0.197\nWind:\t\t0.198\nNuclear:\t0.197\nTOTAL:\t\t4009000";
//
//    @Test
//    public void testDefault() {
//        PowerSplit ps = new PowerSplit();
//        Assert.assertTrue(ps.toString().equals(defaultOut));
//    }
//
//    @Test
//    public void testFromFile() {
//        PowerSplit ps = new PowerSplit("US-2020");
//        Assert.assertTrue(ps.toString().equals(us_2020Out));
//    }
//}
