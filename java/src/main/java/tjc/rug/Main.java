package tjc.rug;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tjc.rug.controller.FrameController;
import tjc.rug.controller.KeyboardShortcuts;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Image icon = new Image (getClass().getResourceAsStream("/media/logo.png"));
        primaryStage.setTitle("Tax vs Trade");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, FrameController.defaultWidth, FrameController.defaultHeight);
        scene.setFill(Color.TRANSPARENT);
        new KeyboardShortcuts(scene);
        primaryStage.setScene(scene);
        FrameController.setStage(primaryStage);
        primaryStage.getIcons().add(icon); // FIXME: Not working, but no error shown. Image is correctly read in.
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
