package tjc.rug.controller;

import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import tjc.rug.model.PowerType;

public class Settings {

    private final String css = this.getClass().getResource("/css/settings.css").toExternalForm();
    private int coalCount = 0;
    private int gasCount = 0;
    private int windCount = 0;
    private int nuclearCount = 0;

    Parent settings;

    public Settings() {
        settings = generatePowerOptions(PowerType.Type.COAL);
    }

    public Parent getSettings() {
        return settings;
    }

    private AnchorPane generatePowerOptions(PowerType.Type type) {
        AnchorPane powerPane = new AnchorPane();
        powerPane.getStylesheets().add(css);
        powerPane.getStyleClass().add("power-pane");
//        setPaneText(powerPane, PowerType.toString(type));
        addInput(powerPane, type);
        //todo: more...

        return powerPane;
    }

    private void addInput(AnchorPane powerPane, PowerType.Type type) {
        Text counter = new Text("0");
        counter.getStyleClass().add("text");
        AnchorPane.setRightAnchor(counter, 10.0);
        AnchorPane.setTopAnchor(counter, 10.0);
        powerPane.getChildren().add(counter);
        Slider slider = new Slider(0, 500, 0); // Min, max, initial
        slider.valueProperty().addListener(
                (observableValue, number, t1) -> { updateCount(type, counter, t1.intValue()); }
        );
        AnchorPane.setTopAnchor(slider, 35.0);
        AnchorPane.setLeftAnchor(slider, 10.0);
        powerPane.getChildren().add(slider);
        CheckBox override = new CheckBox();
        TextField overrideIn = new TextField("Override");
        AnchorPane.setBottomAnchor(override, 15.0);
        AnchorPane.setLeftAnchor(override, 15.0);
        powerPane.getChildren().add(override);
        AnchorPane.setBottomAnchor(overrideIn, 10.0);
        AnchorPane.setRightAnchor(overrideIn, 10.0);
        powerPane.getChildren().add(overrideIn);
    }

    private void updateCount(PowerType.Type type, Text counter, int value) {
        counter.setText(String.valueOf(value));
        System.out.println(value);
    }

    private void setPaneText(AnchorPane powerPane, String name) {
        Text title = new Text(name);
        title.getStyleClass().add("text");
        AnchorPane.setTopAnchor(title, 10.0);
        AnchorPane.setLeftAnchor(title, 10.0);
        powerPane.getChildren().add(title);
    }

//    private // todo: add function setting a node into the anchor pane

}
