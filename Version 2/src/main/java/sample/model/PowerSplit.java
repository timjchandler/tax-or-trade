package sample.model;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Handles the settings for starting a simulation. Holds the proportion of each energy production type and the
 * total maximum energy to be produced in Gigawatt Hours
 */
public class PowerSplit {

    private float gas;      // The proportion of the power generated by Gas powerplants
    private float wind;     // As above, for wind
    private float coal;     // As above, for coal
    private float nuclear;  // As above, for nuclear
    private int total;      // The total power that may be generated

    /**
     * Constructor
     * Sets all proportions equally, sets the total to a default value
     */
    public PowerSplit() {
        this.gas = this.coal = this.wind = this.nuclear = .25f;
        this.total = 100000;
    }

    /**
     * Overloaded constructor
     * Loads a setup from a file
     * @param file The file from which to load
     */
    public PowerSplit(String file) {
        this.setFromFile(file);
    }

    /**
     * Reads in a file and sets the member variables accordingly
     * @param filename  The file to read in
     */
    public void setFromFile(String filename) {
        URL resource = getClass().getClassLoader().getResource("Setups/" + filename);
        try {
            assert resource != null;
            File file = new File(resource.toURI());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String in = br.readLine();
            while (in != null) {
                switch (in.substring(0, 2)) {
                    case ("//"):
                        break;
                    case ("G:"):
                        this.gas = Float.parseFloat(in.substring(2));
                        break;
                    case ("C:"):
                        this.coal = Float.parseFloat(in.substring(2));
                        break;
                    case ("W:"):
                        this.wind = Float.parseFloat(in.substring(2));
                        break;
                    case ("N:"):
                        this.nuclear = Float.parseFloat(in.substring(2));
                        break;
                    case ("T:"):
                        this.total = Integer.parseInt(in.substring(2));
                }
                in = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println(":: ERROR: File not found (PowerSplit)");
        } catch (IOException e) {
            System.out.println(":: ERROR: Invalid File (PowerSplit)");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gas:\t\t").append(gas).append("\n");
        sb.append("Coal:\t\t").append(coal).append("\n");
        sb.append("Wind:\t\t").append(wind).append("\n");
        sb.append("Nuclear:\t").append(nuclear).append("\n");
        sb.append("TOTAL:\t\t").append(total);
        return sb.toString();
    }

    // Getters

    float getGas() {
        return gas;
    }

    public float getWind() {
        return wind;
    }

    public float getCoal() {
        return coal;
    }

    public float getNuclear() {
        return nuclear;
    }
}