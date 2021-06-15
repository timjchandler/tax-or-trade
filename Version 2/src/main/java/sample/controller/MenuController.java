package sample.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Main;
import sample.model.World;

import java.io.IOException;
import java.util.Objects;

public class MenuController implements EventHandler {
    private static World world;
    private Controller controller;

    public MenuController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void handle(Event event) {
        Button source = (Button) event.getSource();
        switch (source.getText()) {
            case "Start":
                start();
                break;
            case "Set seed":
                seed();
                break;
        }
    }

    private void seed() {
        Stage dialogue = new Stage(StageStyle.UNDECORATED);
        dialogue.initOwner(Main.getPrimaryStage());
        try {
            Parent box = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/SeedPopup.fxml")));
            dialogue.initStyle(StageStyle.UNDECORATED);
            dialogue.setScene(new Scene(box, 300, 150));
            dialogue.show();
        } catch (IOException e) {
            System.out.println("::ERROR:: Menu Controller - cannot load SeedPopup.fxml");
        }
    }

    private void start() {
        controller.updateBottomText();
        world.start();
    }

    public static void setWorld(World world) {
        MenuController.world = world;
    }
}