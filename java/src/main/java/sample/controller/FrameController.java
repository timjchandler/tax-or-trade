package sample.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
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

    @FXML
    private BorderPane mainPane;

    @FXML
    private VBox consoleBox;

    @FXML
    private VBox leftBar;

    @FXML
    private VBox lowerLeftBar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        if (mainPane != null) /*stage = (Stage)*/ mainPane.getScene();//.getWindow();
        new Console(consoleBox);
        loadPage("loading");
        initDraggable(leftBar);
        initDraggable(lowerLeftBar);
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
//        setStage();
        shutDown(stage);
    }

    @FXML
    private void minimize(ActionEvent event) {
//        setStage();
        stage.setIconified(true);
        Console.print(":: Minimized");
    }

    @FXML
    private void maximize(ActionEvent event) {
//        setStage();
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

//    public static void shutDown() {
//        if (stage != null) shutDown(stage);
//    }

    public static void shutDown(Stage stage) {
        stage.close();
        System.out.println(":: Shutting down");
        System.exit(0);
    }
}
