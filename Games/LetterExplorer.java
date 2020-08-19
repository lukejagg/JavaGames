package Games;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class LetterExplorer extends Application {

    static int offsetX = 0;
    static int offsetY = 0;

    static int cellSize = 16;
    static int width = 50;
    static int height = 50;

    static int[][] chars = new int[width][height];

    static boolean move = false;
    static String search = "";

    static int m = 25;

    static GraphicsContext gc;

    static Random r = new Random();

    static boolean started = false;
    static boolean testType = false;

    static AnimationTimer timer = new AnimationTimer() {

        int i = 0;
        int j = 0;

        @Override
        public void handle(long now) {
            for (int i = 0; i < 1000; i++) {
                if (!testType) {
                    offsetX = (r.nextInt(2147450000) + Integer.MIN_VALUE / 2) * 2;
                    offsetY = (r.nextInt(2147450000) + Integer.MIN_VALUE / 2) * 2;
                    draw();
                } else {
                    i += m;
                    if (i > m * 20000) {
                        j += m;
                        i = 0;
                    }

                    offsetX = i;
                    offsetY = j;
                    draw();

                }
                if (!started)
                    return;
            }
        }
    };

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene s = new Scene(root, width * cellSize, height * cellSize, Color.WHITE);

        final Canvas canvas = new Canvas(width * cellSize, height * cellSize);
        canvas.setFocusTraversable(true);

        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Letter Generator");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        draw();

        canvas.setOnKeyPressed(e -> {
            boolean changed = false;
            switch (e.getCode()) {
                case T:
                    if (move) {
                        started = !started;
                        if (started)
                            timer.start();
                        else
                            timer.stop();
                    }
                    break;
                case Y:
                    testType = !testType;
                    break;
                case SHIFT:
                    move = !move;
                    break;
                case W:
                    if (move) {
                        offsetY -= m;
                        draw();
                    }
                    break;
                case S:
                    if (move) {
                        offsetY += m;
                        draw();
                    }
                    break;
                case D:
                    if (move) {
                        offsetX += m;
                        draw();
                    }
                    break;
                case A:
                    if (move) {
                        offsetX -= m;
                        draw();
                    }
                    break;
                case SPACE:
                    search = "";
                    draw();
                    changed = true;
                    break;
            }
            if (!changed && !move && e.getText().toCharArray().length > 0) {
                search += e.getText().toUpperCase();
                draw();
            }
        });

    }

    public static void draw() {
        gc.clearRect(0, 0, width * cellSize, height * cellSize);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int x = i + offsetX;
                int y = j + offsetY;
                // TODO: make dank memez explorerz
                int letter = x*x*y+y*(x^y)|x+y^y*y;
                letter %= 26;
                if (letter < 0)
                    letter += 26;
                letter += 65;
                chars[i][j] = letter;
                gc.setFill(Color.BLACK);
                gc.setFont(Font.font(null, FontWeight.BOLD, cellSize));
                gc.fillText(Character.valueOf((char) letter).toString(), i * cellSize + cellSize / 2, j * cellSize + cellSize / 2);
//                if (letter == cL) {
//                    gc.setFill(Color.RED);
//                    gc.setFont(Font.font(null, FontWeight.NORMAL, 20 ));
//                    gc.fillText(Character.valueOf((char) letter).toString(), i * 20 + 10, j * 20 + 10);
//                }
            }
        }
        int[] s = new int[search.length()];
        for (int i = 0; i < s.length; i++) {
            s[i] = search.toUpperCase().charAt(i);
        }
        boolean works = false;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int d = 0; d < 8; d++) {
                    int a = 0;
                    int b = 0;
                    switch (d) {
                        case 0:
                            a = 1;
                            break;
                        case 1:
                            a = 1;
                            b = 1;
                            break;
                        case 2:
                            b = 1;
                            break;
                        case 3:
                            a = -1;
                            b = 1;
                            break;
                        case 4:
                            a = -1;
                            break;
                        case 5:
                            a = -1;
                            b = -1;
                            break;
                        case 6:
                            b = -1;
                            break;
                        case 7:
                            a = 1;
                            b = -1;
                            break;
                    }
                    works = true;
                    for (int l = 0; l < search.length(); l++) {
                        if (i + l * a < 0 || i + l * a >= width || j + l * b < 0 || j + l * b >= height || s[l] != chars[i + l * a][j + l * b])
                        {
                            works = false;
                            break;
                        }
                    }
                    if (works) {
                        for (int l = 0; l < search.length(); l++) {
                            if (started) {
                                System.out.println(i + " " + j);
                                timer.stop();
                                started = false;
                            }
                            gc.setFill(Color.BLACK);
                            gc.fillRect((i + l * a) * cellSize, (j + l * b) * cellSize, cellSize, cellSize);
                            gc.setFill(Color.ORANGE);
                            gc.fillText(Character.valueOf((char) chars[i + l * a][j + l * b]).toString(), (i + l * a) * cellSize + cellSize / 2, (j + l * b) * cellSize + cellSize / 2);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
