package sample.controller;

import sample.model.World;

public abstract class AbstractController {

    private static Controller mainController;
    private static World world;

    public static void setMainController(Controller mainController) {
        AbstractController.mainController = mainController;
    }

    public static void setWorld(World world) {
        AbstractController.world = world;
    }

    public static Controller getMainController() {
        return mainController;
    }

    public static World getWorld() {
        return world;
    }
}
