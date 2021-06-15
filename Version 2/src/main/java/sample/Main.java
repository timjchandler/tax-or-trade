package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.controller.Controller;
import sample.model.Agent;
import sample.model.World;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        World world = new World(31);
        Controller.setWorld(new World(31));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/View.fxml")));
        primaryStage.setTitle("Tax vs. Trade");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
