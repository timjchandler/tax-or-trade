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

import java.io.IOException;
import java.util.Objects;

public class MenuController extends AbstractController implements EventHandler {

    @Override
    public void handle(Event event) {
        Button source = (Button) event.getSource();
        switch (source.getText()) {
            case "Start":
                start();
                break;
            case "Set Seed":
                seed();
                break;
            case "Set Parameters":
                initialValues();
                break;
        }
    }

    private void initialValues() {
        Stage dialogue = new Stage(StageStyle.UNDECORATED);
        dialogue.initOwner(Main.getPrimaryStage());
        try {
            Parent box = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ParametersPopup.fxml")));
            dialogue.setScene(new Scene(box, 300, 350));
            dialogue.show();
        } catch (IOException e) {
            System.out.println("::ERROR:: Menu Controller - cannot load ParametersPopup.fxml");
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
        getMainController().updateBottomText();
        getWorld().start();
    }
}