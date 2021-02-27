package sample.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.model.Power;
import sample.model.PowerType;

import java.nio.file.Files;

public class Console {

    private final static AnchorPane inBox = new AnchorPane();
    private final static TextArea consoleView = new TextArea();
    private final static TextField consoleIn = new TextField();
    private final static Text prompt = new Text(" > ");
    private static String help = null;

    public Console(VBox consoleArea) {
        if (help == null) setHelp();
        initConsole(consoleArea);
        print(":: Console initialised");
    }

    public static void print(String string) {
        consoleView.appendText(string);
        consoleView.appendText(System.lineSeparator());
        System.out.println(string);
    }

    private void setHelp() {

//        help = Files.readString
    }

    private void initConsole(VBox consoleArea) {
        AnchorPane.setLeftAnchor(prompt, 10.0);
        AnchorPane.setBottomAnchor(prompt, 6.0);
        inBox.getChildren().add(prompt);
        AnchorPane.setLeftAnchor(consoleIn, 25.0);
        AnchorPane.setRightAnchor(consoleIn, 0.0);
        AnchorPane.setBottomAnchor(consoleIn, 0.0);
        inBox.getChildren().add(consoleIn);
        consoleArea.getChildren().add(consoleView);
        consoleArea.getChildren().add(inBox);
        consoleArea.getStylesheets().add("/css/console.css");
        inBox.getStyleClass().add("in-box");
        prompt.getStyleClass().add("prompt");
        consoleView.getStyleClass().add("console-view");
        consoleView.setEditable(false);
        consoleIn.getStyleClass().add("console-in");
        print(":: Welcome");
        initInput();
    }

    private void initInput() {
        consoleIn.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            switch (keyEvent.getCode()) {       // TODO: Add more commands (history arrows, etc.)
                case ENTER:
                    print(" > " + consoleIn.getText());
                    handleInput(consoleIn.getText().toLowerCase());
                    consoleIn.clear();
            }
        });
    }

    private void handleInput(String input) {
        switch (input) {                        // TODO: Add more inputs
            case "clear-all":
                consoleView.clear();
                break;
            case "exit":
                FrameController.shutDown((Stage) consoleIn.getScene().getWindow());
                break;
            case "hello":
                print("Hi there.");
                break;
            case "new-power":
                Power p = new Power(new PowerType(PowerType.Type.COAL));
                Console.print(p.toString());
                break;
            case "start-run":
                FrameController.startRun();
                break;
            case "stop":
                FrameController.stopRun();
                break;
            case "restart-run":
                FrameController.restartRun();
                break;
            case "settings":
                FrameController.toSettings();
                break;
            case "help":
                print(help);
                break;
            case "":
                break;
            default:
                print("Unknown command \"" + input + "\"\n Enter 'help' for available commands");
        }
    }
}
