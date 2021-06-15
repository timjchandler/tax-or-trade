package sample.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.model.World;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private static World world;
    private boolean initialised = false;

    @FXML
    private VBox menuBar;

    @FXML
    private Button btnMin;

    @FXML
    private TextArea topInfoA;

    @FXML
    private TextArea topInfoB;

    @FXML
    private TextArea topInfoC;

    @FXML
    private Pane mainArea;

    @FXML
    private Text bottomText;

    public void init() {
        topInfoA.setEditable(false);
        topInfoB.setEditable(false);
        topInfoC.setEditable(false);
        initMenu();
        world.setController(this);
    }

    private void initMenu() {
        MenuController mc = new MenuController(this);
        Button start = new Button("Start");
        start.setOnAction(mc);
        start.getStylesheets().add("css/mainMenu.css");
        Button seed = new Button("Set seed");
        seed.setOnAction(mc);
        seed.getStylesheets().add("css/mainMenu.css");

        this.menuBar.getChildren().add(start);
        this.menuBar.getChildren().add(seed);
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void minimise(ActionEvent event) {
        ((Stage) btnMin.getScene().getWindow()).setIconified(true);
    }

    public void updateTick(int tick) {
        topInfoA.setText("Tick:\t" + tick);
    }

    public static void setWorld(World world) {
        Controller.world = world;
        MenuController.setWorld(world);
        System.out.println("::CONTROLLER:: World set");
    }

    public void updateBottomText() {
        bottomText.setText("CSV file saved in:\t" + world.getSaveLocation());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

}
