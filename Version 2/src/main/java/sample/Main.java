package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.model.Agent;
import sample.model.World;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("/view/View.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
        test();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void test() {
        World world = new World(31);
        for (int idx = 0; idx < 10; ++idx)
            world.addAgent(new Agent(idx, (idx * 10) % 7, world.getTick()));

        world.tick();
    }
}
