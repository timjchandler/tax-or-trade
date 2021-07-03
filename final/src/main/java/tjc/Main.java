package tjc;

import tjc.model.World;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) help();
        else {
            int runs = Integer.parseInt(args[0]);
            int seed = Integer.parseInt(args[1]);
            int isTax = Integer.parseInt(args[2]);
            for (int idx = 0; idx < runs; ++ idx) {
                World world = new World(idx + seed);
                if (isTax == 1) world.setTaxOrTrade(true);
                else world.setTaxOrTrade(false);
                world.start();
            }
            System.out.println("Finished " + runs + " runs with seeds in the range [" + seed + ", " + (seed + runs - 1) + "]");
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
