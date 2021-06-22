package model;

import org.junit.Assert;
import org.junit.Test;
import sample.model.agent.Agent;
import sample.model.power.Power;
import sample.model.power.PowerType;

public class AgentTest {

//    @Test
//    public void testAgent() {
//        Agent agent = new Agent(1, 0);
//        Assert.assertEquals(1, agent.getId());
//        Assert.assertEquals(100, agent.getMoney());
//        Assert.assertEquals(0, agent.getTick());
//    }
//
//    @Test
//    public void testAddPower() {
//        Agent agent = new Agent(1, 0);
//        agent.addPower(new Power(PowerType.COAL));
//        agent.addPower(new Power(PowerType.GAS));
//        agent.printPower();
//        agent.update(2, 0.1f);
//        agent.printPower();
//    }
    @Test
    public void testAgent() {
        Agent agent = new Agent(0, 0);
        for (int idx = 0; idx < 10; ++idx) {
            agent.addPower(new Power(PowerType.WIND));
            if (idx < 5) agent.addPower(new Power(PowerType.COAL));
            if (idx < 3) agent.addPower(new Power(PowerType.GAS));
        }
        agent.setRequired(1000);
        agent.addPower(new Power(PowerType.NUCLEAR));
        agent.updatePower(1);
//        System.out.println(agent.updatePower(1));
    }

}
