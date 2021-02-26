package sample.controller;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class KeyboardShortcuts {

    public KeyboardShortcuts(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.W, KeyCombination.META_DOWN);
            final KeyCombination AlternativeKeyComb = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke) || AlternativeKeyComb.match(ke)) {
                    Stage stage = (Stage) scene.getWindow();
                    FrameController.shutDown(stage);
                    ke.consume();
                }
            }
        });
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                Stage stage = (Stage) scene.getWindow();
                switch (ke.getCode()) {                         // TODO: More shortcuts added here as required
                    case F11:
                        FrameController.manageMaximize(stage);  // FIXME: This is exhibiting strange behaviour
                        break;
                }

            }
        });
    }

}
