package sample.model;

import java.util.ArrayList;

public class Agent {

    private int id;
    private int money;
    private float carbon;
    private float electricity;
    private ArrayList<Power> power;
    private int tick;

    public Agent(int id, int initialMoney, int tick) {
        this.id = id;
        this.money = initialMoney;
        this.carbon = 0;
        this.electricity = 0;
        this.tick = tick;
        this.power = new ArrayList<>();
    }

    public void update(int tick) {
        if (tick == this.tick) return;
        this.tick = tick;
        // TODO: more here
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(',');
        sb.append(money).append(',');
        sb.append(carbon).append(',');
        sb.append(electricity).append(',');
        sb.append("n/a").append(',');       // TODO: Should hold a value or be removed
        sb.append(tick);
        return sb.toString();
    }
}
