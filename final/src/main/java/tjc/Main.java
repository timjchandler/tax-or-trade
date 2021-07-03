package tjc;

import tjc.model.World;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) help();
        else {
            int runs = Integer.parseInt(args[0]);
            int seed = Integer.parseInt(args[1]);
            for (int idx = 0; idx < runs; ++ idx) {
                World world = new World(idx + seed);
                world.setTaxOrTrade(true);
                world.start();
            }
            System.out.println("Finished " + runs + " Tax runs with seeds in the range [" + seed + ", " + (seed + runs - 1) + "]");
            for (int idx = 0; idx < runs; ++ idx) {
                World world = new World(idx + seed);
                world.setTaxOrTrade(false);
                world.start();
            }
            System.out.println("Finished " + runs + " Trade runs with seeds in the range [" + seed + ", " + (seed + runs - 1) + "]");
        }
    }

    private static void help() {
        System.out.println("Run with the following arguments:\n" +
                "-\tNumber of runs\t\t\t(Integer)" +
                "-\tSeed number\t\t\t\t(Integer)\n" +
                "-\tTax rate\t\t\t\t(Float)\n" +
                "-\tYearly tax increment\t(Float)\n" +
                "This may be run with 1 or 3 arguments.");
        System.exit(0);
    }
}
