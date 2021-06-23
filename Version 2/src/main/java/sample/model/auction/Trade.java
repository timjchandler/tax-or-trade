package sample.model.auction;

import sample.model.AbstractTick;
import sample.model.World;

public class Trade extends AbstractTick {

    private float cap;
    private float capChange;

    public Trade(World world, float cap, float capChange) {
        super(world);

    }


    @Override
    public int tick() {
        return super.tick();
    }
}
