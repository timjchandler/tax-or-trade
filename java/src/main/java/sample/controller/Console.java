package sample.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Console {

    private final static TextArea consoleView = new TextArea();
    private final static TextField consoleIn = new TextField();

    public Console(VBox consoleArea) {
        consoleArea.getChildren().add(consoleView);
        consoleArea.getChildren().add(consoleIn);
        consoleArea.getStylesheets().add("/css/console.css");
        initConsole();
        print(":: Console initialised");
    }

    public static void print(String string) {
        consoleView.appendText(string);
        consoleView.appendText(System.lineSeparator());
        System.out.println(string);
    }

    private void initConsole() {
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
                    handleInput(consoleIn.getText());
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
        }
    }
}
