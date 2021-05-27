package tjc.rug.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tjc.rug.model.World;

import java.util.ArrayList;

public class Console {

    private final static AnchorPane inBox = new AnchorPane();
    private final static TextArea consoleView = new TextArea();
    private final static TextField consoleIn = new TextField();
    private final static Text prompt = new Text(" > ");
    private final static ArrayList<String> history = new ArrayList<>();
    private static String help = null;
    private static int historyIdx = 0;

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
//        InputStream helpStream = getClass().getResourceAsStream("/media/help.txt");
//        try {
//            help = IOUtils.toString(helpStream, StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void initConsole(VBox consoleArea) {
        AnchorPane.setLeftAnchor(prompt, 10.0);
        AnchorPane.setBottomAnchor(prompt, 6.0);
        inBox.getChildren().add(prompt);
        AnchorPane.setLeftAnchor(consoleIn, 25.0);
        AnchorPane.setRightAnchor(consoleIn, 10.0);
        AnchorPane.setBottomAnchor(consoleIn, 0.0);
        inBox.getChildren().add(consoleIn);
        consoleArea.getChildren().add(consoleView);
        consoleArea.getChildren().add(inBox);
        String css = this.getClass().getResource("/css/console.css").toExternalForm();
        consoleArea.getStylesheets().add(css);
        inBox.getStyleClass().add("in-box");
        prompt.getStyleClass().add("prompt");
        consoleView.getStyleClass().add("console-view");
        consoleView.setEditable(false);
        consoleIn.getStyleClass().add("console-in");
        print(":: Version: PROTOTYPE");
        initInput();
    }

    private void initInput() {
        consoleIn.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case ENTER:                     // Enter a command
                    String in = consoleIn.getText();
                    print(" > " + in);
                    handleInput(in.toLowerCase());
                    if (!in.equals("")) history.add(0, in);
                    historyIdx = 0;
                    consoleIn.clear();
                    break;
                case UP:                        // Cycle up through previous commands
                    if (history.size() > historyIdx) {
                        consoleIn.setText(history.get(historyIdx));
                        ++historyIdx;
                    }
                    break;
                case DOWN:                      // Cycle down through previous commands
                    if (historyIdx <= 1) {
                        consoleIn.clear();
                        historyIdx = 0;
                    }
                    else {
                        --historyIdx;
                        consoleIn.setText(history.get(Math.max(0, historyIdx - 1)));
                        break;
                    }
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
            case "new-power":                   // Debug/test command
//                Power p = new Power(new PowerType(PowerType.Type.COAL));
//                Console.print(p.toString());
                Console.print("Currently not doing anything");
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
            case "help":
                print(help);
                break;
            case "new-world":
                new World(100, 0.1f, 0.3f);
                Console.print(":: Setting up World ...");
                break;
            case "run-world":
                Console.print(":: Running World ...");
                World.run();
                break;
            case "":
                break;
            default:
                print("Unknown command \"" + input + "\"\nEnter 'help' for available commands");
        }
    }
}
