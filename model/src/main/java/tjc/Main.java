package tjc;

/**
 * Main, parses command line arguments and calls the runner class.
 * Shows an error message if the command line arguments are incorrectly formatted.
 */
public class Main {

    /**
     * Called on running the program, parses the command line arguments and passes them to Runner.
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            help();
            return;
        }
        else {
            int runs = Integer.parseInt(args[0]);
            int seed = Integer.parseInt(args[1]);
            int tax = Integer.parseInt(args[2]);
            float trade = Float.parseFloat(args[3]);
            int ticks = Integer.parseInt(args[4]);
            String preset = args[5];
            new Runner(runs, seed, ticks, tax, trade, preset);
        }
    }

    /**
     * Shows an explanation of how to run the program if the command line arguments are missing.
     */
    private static void help() {
        System.out.println(":: HELP ::\n" +
                "To run the simulation please provide the following command line arguments:\n" +
                "\t- Runs: An integer representing the number of runs you would like to simulate.\n" +
                "\t- Seed: An integer representing the initial seed to run the simulation from.\n" +
                "\t- Tax: An integer representing the target tax rate in euros per tonne of CO2.\n" +
                "\t- Trade: A float representing the yearly reduction in Carbon Cap in percent.\n" +
                "\t- Preset: A string representing the starting conditions {fifty, seventy or ninety}");
    }
}
