package sample.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import sample.model.World;

public class MenuController implements EventHandler {
    private static World world;
    private Controller controller;

    public MenuController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void handle(Event event) {
        String loc = world.getSaveLocation();
        controller.updateBottomText("CSV saved in:\t" + loc);
        world.start();
    }

    public static void setWorld(World world) {
        MenuController.world = world;
    }
}