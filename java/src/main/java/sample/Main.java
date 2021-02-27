package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.controller.FrameController;
import sample.controller.KeyboardShortcuts;

import javax.swing.*;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        primaryStage.setTitle("Tax vs Trade");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root, FrameController.defaultWidth, FrameController.defaultHeight);
        new KeyboardShortcuts(scene);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/media/logo.png")));
        FrameController.setStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Update task bar logo on MacOS
        try {
            URL iconURL = Main.class.getResource("/media/logo.png");
            java.awt.Image image = new ImageIcon(iconURL).getImage();
//            com.apple.eawt.Application.getApplication().setDockIconImage(image);
        } catch (Exception e) {
        }
        launch(args);
    }


}
