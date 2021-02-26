package sample.controller;

import javafx.animation.RotateTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Animate {

    private static boolean stop;

    public static void setRotate(Pane pane, float timeRotation, boolean reverse) {
        stop = false;
        RotateTransition rt = new RotateTransition(Duration.seconds(timeRotation), pane);
        rt.setAutoReverse(reverse);
        rt.setByAngle(360);
        rt.setCycleCount(2000);         // Arbitrary large number
        rt.play();
    }

    public static void updateLoadingBar(Pane bar, int percent, String color) {
        StringBuilder sb = new StringBuilder("-fx-background-color: linear-gradient(to right,");
        sb.append(color);
        sb.append(Math.max(0, percent - 5));
        sb.append("%, rgba(0,0,0,0) ");
        sb.append(Math.min(100, percent + 5));
        sb.append("%);");
        bar.setStyle(sb.toString());
    }

//    public static void changeGradient(Pane pane, Color colorOne, Color colorTwo, float time) {
//        stop = false;
//        double lastCall = 0;
//        int steps = 0;
//        AnimationTimer timer = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                if (now > lastCall + 50) {
//
//                }
//            }
//        }
//    }

}
