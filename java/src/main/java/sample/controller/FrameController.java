package sample.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FrameController implements Initializable {

    private static boolean isMaximized = false;
    private static boolean dragInitialised = false;
    private static Stage stage = null;
    private static double xOffset;
    private static double yOffset;

    public static final int defaultWidth = 600;
    public static final int defaultHeight = 800;

    @FXML
    private BorderPane mainPane;

    @FXML
    private VBox consoleBox;

    @FXML
    private VBox leftBar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        if (mainPane != null) /*stage = (Stage)*/ mainPane.getScene();//.getWindow();
        new Console(consoleBox);
        loadPage("loading");
    }

    private void loadPage(String page) {
        Parent root = null;
        String path = "/fxml/" + page + ".fxml";
        try {
            root = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.setCenter(root);
    }

    @FXML
    private void close(ActionEvent event) {
        setStage();
        shutDown(stage);
    }

    @FXML
    private void minimize(ActionEvent event) {
        setStage();
        stage.setIconified(true);
        Console.print(":: Minimized");
    }

    @FXML
    private void maximize(ActionEvent event) {
        setStage();
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

    @FXML
    private void initDraggable() {
        if (dragInitialised) return;
        dragInitialised = true;
        setStage();
        leftBar.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        leftBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }

    private void setStage() {
        if (stage == null) {
            stage = (Stage) mainPane.getScene().getWindow();
        }
    }

//    public static void shutDown() {
//        if (stage != null) shutDown(stage);
//    }

    public static void shutDown(Stage stage) {
        stage.close();
        System.out.println(":: Shutting down");
        System.exit(0);
    }
}
