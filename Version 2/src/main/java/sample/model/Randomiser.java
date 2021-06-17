package sample.model;

import java.util.Random;

public abstract class Randomiser {

    private static Random random = null;
    private static int seed;

    /**
     * Sets the seed for the random number generation
     * @param seed  The seed to be set
     */
    public static void setSeed(int seed) {
        Randomiser.seed = seed;
        random = new Random(seed);
    }

    /**
     * Gets a value from a normal distribution as defined by the input arguments.
     * @param mean      The mean value of the distribution
     * @param sd        The standard deviation of the distribution
     * @param cutoff    The number of sd's to allow the output to fall within (0 for no limitation)
     * @return          A value from the normal distribution.
     */
    private float getNormal(float mean, float sd, float cutoff) {
        if (random == null) setSeed(0);
        float out = (float) random.nextGaussian() * mean + sd;
        while (cutoff != 0 && (out < mean + cutoff * sd && out > mean - cutoff * sd))
            out = (float) random.nextGaussian() * mean + sd;
        return out < 0 ? 0 : out;
    }

    /**
     * Overloaded method. Get a value from a normal distribution with no cutoff.
     * @param mean      The mean value of the distribution
     * @param sd        The standard deviation of the distribution
     * @return          A value from the normal distribution.
     */
    public float getNormal(float mean, float sd) {
        return getNormal(mean, sd, 0);
    }

    /**
     * Overloaded method. Get a value from a normal distribution with no cutoff and
     * an a standard deviation of 5% of the mean.
     * @param mean      The mean value of the distribution
     * @return          A value from the normal distribution.
     */
    public float getNormal(float mean) {
        return getNormal(mean, mean * 0.05f);
    }

    /**
     * Gets a random integer value
     * @return A randomly selected integer
     */
    public int getInt() {
        return random.nextInt();
    }

    /**
     * Gets a random integer value within a range
     * @param max   The maximum value to return
     * @return      The randomly selected integer
     */
    public int getInt(int max) {
        if (max <= 0) {
            System.out.println("ERROR: Negative bound");
            return 1;
        }
        return random.nextInt(max);
    }

    public static int getSeed() {
        return seed;
    }
}
