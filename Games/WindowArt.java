package Games;

/// README:
/// WARNING: THIS MAY FREEZE YOUR COMPUTER
/// TO EXIT, PRESS ALT + F4, OR CMD + Q
/// THIS IS JUST A TEST PROGRAM

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class WindowArt extends Application {

    static int CELL_SIZE = 50, WIDTH = 10, HEIGHT = 10, i = 0, j = 0;
    static ArrayList<Stage> stages = new ArrayList<Stage>();

    public static void initializeStage(Stage stage, int px, int py, int sx, int sy) {
        Group root = new Group();
        Scene s = new Scene(root, sx, sy, Color.hsb((10 * px + 10 * py) / CELL_SIZE, 1, 1));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.setScene(s);
        moveStage(stage, px, py);
        stage.show();
    }

    public static void moveStage(Stage s, double x, double y) {
        s.setX(x);
        s.setY(y);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        new AnimationTimer() {
            @Override
            public void handle(long t) {
//                for (Stage s : stages) {
//                    moveStage(s, s.getX() + 1, s.getY() + 1);
//                }
                for (int l = 0; l < 15; l++) {
                    Stage s = new Stage();
                    initializeStage(s, i * CELL_SIZE, j * CELL_SIZE + 22, CELL_SIZE, CELL_SIZE);
                    stages.add(s);
                    if (i++ >= 30) {
                        i = 0;
                        if (j++ > 16) {
                            j = 0;
                        }
                    }
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
