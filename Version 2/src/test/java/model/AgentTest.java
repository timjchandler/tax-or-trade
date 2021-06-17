package model;

import org.junit.Assert;
import org.junit.Test;
import sample.model.Agent;
import sample.model.Power;
import sample.model.PowerType;

public class AgentTest {

    @Test
    public void testAgent() {
        Agent agent = new Agent(1, 100, 0);
        Assert.assertEquals(1, agent.getId());
        Assert.assertEquals(100, agent.getMoney());
        Assert.assertEquals(0, agent.getTick());
    }

    @Test
    public void testAddPower() {
        Agent agent = new Agent(1, 100, 0);
        agent.addPower(new Power(PowerType.COAL));
        agent.addPower(new Power(PowerType.GAS));
        agent.printPower();
        agent.update(2, 0.1f);
        agent.printPower();
    }

}