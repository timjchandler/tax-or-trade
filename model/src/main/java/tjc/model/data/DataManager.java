package tjc.model.data;

import tjc.model.World;
import tjc.model.power.Power;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Manages the data from the model, compiles it into a string at the end of each tick and
 * appends it to a csv file
 */
public class DataManager {

    private final World world;      // The world of the model
    private File save;              // The file in which this models data is saved
    private final DataCGNW data;    // The store of the raw data

    /**
     * Constructor, stores the world of the model, zeroes the data values and creates a file for storing
     * the data
     * @param world     The world of the model
     */
    public DataManager(World world) {
        this.world = world;
        data = DataCGNW.RESET;
        createFile();
    }

    /**
     * Creates a file with a name generated based on the setup of the current simulation. If a file with
     * that configuration already exists it is replaced. Shows the user error messages if the file creation
     * or deletion is unsuccessful
     */
    private void createFile() {
        save = new File(world.getSaveName());
        if (save.exists()) {
             if (!save.delete()) System.out.println(":: ERROR: File " + world.getSaveName() + " could not be removed for replacement.");
        }
        try {
            if (!save.createNewFile()) System.out.println(":: ERROR: The file " + world.getSaveName() + " could not be created. This could be due to a file existing with that name that was not cleaned up.");
        } catch (IOException e) {
            System.out.println(":: ERROR: Could not create file " + world.getSaveName());
        }
        appendCSV("Tick,Coal_Carbon,Gas_Carbon,Nuclear_Carbon,Wind_Carbon,Coal_Electricity,Gas_Electricity,Nuclear_Electricity,Wind_Electricity\n");
    }

    /**
     * Appends a string the currently stored data to the end of the file for this model configuration.
     * @param string The string to append
     */
    private void appendCSV(String string) {
        try {
            FileWriter fr = new FileWriter(save, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(string);
            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println(":: ERROR: Could not append string to the CSV file at " + save.getPath());
        }
    }

    /**
     * Updates the per tick data on a relevant fuel type based on the input power plant
     * @param p The power plant whose information should be stored
     */
    public void add(Power p) {
        switch (p.getType()) {
            case COAL:
                data.updateCoal(p.getCarbon(), p.getProduction());
                break;
            case GAS:
                data.updateGas(p.getCarbon(), p.getProduction());
                break;
            case NUCLEAR:
                data.updateNuclear(p.getCarbon(), p.getProduction());
                break;
            case WIND:
                data.updateWind(p.getCarbon(), p.getProduction());
        }
    }

    /**
     * Generates a string from the currently stored data, appends it to the csv file and resets the
     * data store to zeroed values.
     * @param tick The tick for which data is being written
     */
    public void write(int tick) {
        StringBuilder sb = new StringBuilder();
        sb.append(tick).append(',');
        sb.append(data.getCoalCarbon()).append(',');
        sb.append(data.getGasCarbon()).append(',');
        sb.append(data.getNuclearCarbon()).append(',');
        sb.append(data.getWindCarbon()).append(',');
        sb.append(data.getCoalElectricity()).append(',');
        sb.append(data.getGasElectricity()).append(',');
        sb.append(data.getNuclearElectricity()).append(',');
        sb.append(data.getWindElectricity()).append('\n');
        appendCSV(sb.toString());
        data.zero();
    }

    /**
     * Getter for the filepath to where the data is being stored
     * @return  The filepath of the stored data as a string
     */
    public String getFilepath() {
        return save.getPath();
    }

    /**
     * Returns the electricity generated this tick
     * @return The electricity generated this tick
     */
    public float getElectricityThisTick() {
        return data.getTotalElectricity();
    }

    /**
     * Gets the CO2 generated this tick
     * @return The CO2 generated this tick
     */
    public float getCarbonThisTick() {
        return data.getTotalCarbon();
    }
}
