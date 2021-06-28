package tjc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tjc.controller.Controller;
import tjc.model.World;

import java.util.Objects;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        World world = new World(31);
        Controller.setWorld(new World(31));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/View.fxml")));
        primaryStage.setTitle("Tax vs. Trade");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (args.length == 0) launch(args);
        else {
            if (args[0].equals("-h") || args[0].equals("help")) help();
            World world = new World(Integer.parseInt(args[0]));
            if (args.length == 3) {
                world.setTaxRate(Float.parseFloat(args[1]));
                world.setTaxIncrement(Float.parseFloat(args[2]));
            }
            world.start();
        }
    }

    private static void help() {
        System.out.println("Run with the following arguments:\n" +
                "-\tSeed number\t\t\t\t(Integer)\n" +
                "-\tTax rate\t\t\t\t(Float)\n" +
                "-\tYearly tax increment\t(Float)\n" +
                "This may be run with 0, 1 or 3 arguments.");
        System.exit(0);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

}
