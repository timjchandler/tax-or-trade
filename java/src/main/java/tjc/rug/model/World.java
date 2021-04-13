package tjc.rug.model;

import java.util.ArrayList;

public class World {

    private ArrayList<Company> companies;
    private float taxStart;
    private float taxEnd;
    private int ticks;

    public World(int totalTicks, float taxStart, float taxEnd) {
        companies = new ArrayList<>();
        this.taxStart = taxStart;
        this.taxEnd = taxEnd;
        initCompanies(30);
    }

    private void initCompanies(int count) {
        for (int idx = 0; idx < count; ++idx) {

        }
    }

}
