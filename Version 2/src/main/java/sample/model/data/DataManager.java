package sample.model.data;

import sample.model.World;
import sample.model.power.Power;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataManager {

    private final World world;
    private File save;
    private DataCGNW data;

    public DataManager(World world) {
        this.world = world;
        data = DataCGNW.RESET;
        createFile();
    }

    private void createFile() {
        save = new File(world.getSaveName());
        if (save.exists()) {
             if (!save.delete()) System.out.println(":: ERROR: File " + world.getSaveName() + " could not be removed for replacement.");
        }
        try {
            if (!save.createNewFile()) System.out.println(":: ERROR: The file " + world.getSaveName() + " could not be created. This could be due to a file existing with that name that was not cleaned up.");
            System.out.println(":: New save file - " + save.getAbsolutePath());
        } catch (IOException e) {
            System.out.println(":: ERROR: Could not create file " + world.getSaveName());
        }
        appendCSV("Tick,Coal_Carbon,Gas_Carbon,Nuclear_Carbon,Wind_Carbon,Coal_Electricity,Gas_Electricity,Nuclear_Electricity,Wind_Electricity\n");
    }

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
}
