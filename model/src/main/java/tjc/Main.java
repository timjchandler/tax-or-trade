package tjc;

import tjc.model.World;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) return;
        else {
            int runs = Integer.parseInt(args[0]);
            int seed = Integer.parseInt(args[1]);
            int tax = Integer.parseInt(args[2]);
            float trade = Float.parseFloat(args[3]);
            int ticks = Integer.parseInt(args[4]);
            String preset = args[5];
            for (int idx = 0; idx < runs; ++ idx) {
                World world = new World(idx + seed);
                world.setTaxOrTrade(true);
                world.setTotalTicks(ticks);
                world.setTax(tax);
                world.setPreset(preset);
                world.start();
            }
            System.out.println("Finished " + runs + " Tax runs with tax rate " + tax + " using seeds in the range [" + seed + ", " + (seed + runs - 1) + "]");
            for (int idx = 0; idx < runs; ++ idx) {
                World world = new World(idx + seed);
                world.setTaxOrTrade(false);
                world.setTotalTicks(ticks);
                world.setCapIncrement(trade);
                world.setPreset(preset);
                world.start();
            }
            System.out.println("Finished " + runs + " Trade runs with cap reduction rate " + trade + " using seeds in the range [" + seed + ", " + (seed + runs - 1) + "]");
        }
    }
}
