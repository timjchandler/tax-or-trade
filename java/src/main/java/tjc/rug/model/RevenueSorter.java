package tjc.rug.model;

import java.util.Comparator;
import java.lang.Float;

public class RevenueSorter implements Comparator<PowerType> {

    @Override
    public int compare(PowerType o1, PowerType o2) {
        if (o1.getCurrentRevenue() < o2.getCurrentRevenue()) return -1;
        if (o1.getCurrentRevenue() > o2.getCurrentRevenue()) return 1;
        return 0;
    }
}
