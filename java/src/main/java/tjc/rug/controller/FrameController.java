package tjc.rug.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FrameController implements Initializable {

    private static boolean isMaximized = false;
    private static Stage stage = null;
    private static double xOffset;
    private static double yOffset;

    public static final int defaultWidth = 600;
    public static final int defaultHeight = 800;
    private static Parent loadingScreen;
    private Settings settings;
//    private static Parent settingsScreen;

    @FXML
    private BorderPane mainPane;

    @FXML
    private VBox consoleBox;

    @FXML
    private VBox leftBar;

    @FXML
    private VBox lowerLeftBar;

    @FXML
    private Button start;

    @FXML
    private Button stop;

    @FXML
    private Button restart;

    @FXML
    private Button settingsButton;

    @FXML
    private Button homeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Console(consoleBox);
        loadingScreen = loadPage("loading");
        settings = new Settings();
//        settingsScreen = loadPage("settings");
        mainPane.setCenter(loadingScreen);
        initDraggable(leftBar);
        initDraggable(lowerLeftBar);
    }

    private Parent loadPage(String page) {
        Parent root = null;
        String path = "/fxml/" + page + ".fxml";
        try {
            root = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        mainPane.setCenter(root);
        return root;
    }

    @FXML
    private void barClick(ActionEvent event) {
        if (event.getSource().equals(start)) startRun();
        else if (event.getSource().equals(stop)) stopRun();
        else if (event.getSource().equals(restart)) restartRun();
        else if (event.getSource().equals(settingsButton)) toSettings();
        else if (event.getSource().equals(homeButton)) goHome();
    }

    public void toSettings() {
        mainPane.setCenter(settings.getSettings());
        Console.print(":: Settings");
    }

    public static void restartRun() {
        Console.print(":: Restart");
    }

    public static void stopRun() {
        Console.print(":: Stop");
    }

    public static void startRun() {
        Console.print(":: Start");
    }

    public  void goHome() {
        mainPane.setCenter(loadingScreen);
        Console.print(":: Home");
    }


    @FXML
    private void close() {
        shutDown(stage);
    }

    @FXML
    private void minimize() {
        stage.setIconified(true);
        Console.print(":: Minimized");
    }

    @FXML
    private void maximize() {
        manageMaximize(stage);
    }

    public static void manageMaximize(Stage stage) {
        ObservableList<Screen> screens = Screen.getScreensForRectangle(new Rectangle2D(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight()));
        Rectangle2D bounds = screens.get(0).getVisualBounds();
        isMaximized = !isMaximized;
        if (isMaximized) {              // Maximise the window
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        }
        else {                          // Return to default size and position
            stage.setWidth(defaultWidth);
            stage.setHeight(defaultHeight);
            stage.setX((bounds.getWidth() - defaultWidth) / 2 );
            stage.setY((bounds.getHeight() - defaultHeight) / 2);
        }
    }

    private void initDraggable(VBox bar) {
        bar.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        bar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }

    public static void setStage(Stage stage) {
        FrameController.stage = stage;
    }

    public static void shutDown(Stage stage) {
        stage.close();
        System.out.println(":: Shutting down");
        System.exit(0);
    }
}
