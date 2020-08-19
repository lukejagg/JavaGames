package Games;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowPong extends Application {

    static Color theme = Color.RED;

    static Stage p1, p2, ball, score;
    static GraphicsContext scoreGC;
    static double p1X = 0, p1Y = 0, p2X = 0, p2Y = 0, ballX = 0, ballY = 0, ballDir = 0.1, ballSpeed = 6, ballAccel = 2;

    static int WIDTH = 1440, HEIGHT = 878, PADDLE_WIDTH = 12, PADDLE_HEIGHT = 150, BALL_SIZE = 60, SCORE_WIDTH = 250, SCORE_HEIGHT = 60, PADDLE_SPEED = 12, BALL_SPEED = 6,
    p1D = 0, p2D = 0, p1S, p2S;

    public static void initializeStage(Stage stage, int width, int height) {
        Group root = new Group();
        Scene s = new Scene(root, width, height, theme);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.setScene(s);
        stage.show();
    }

    public static void moveStage(Stage s, double x, double y) {
        s.setX(x);
        s.setY(y + 22);
    }

    public static void reset(boolean dir) {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballSpeed = BALL_SPEED;
        if (!dir) {
            ballDir = (Math.random() - 0.5) * 0.3;
        }
        else {
            ballDir = Math.PI + (Math.random() - 0.5) * Math.PI * 0.75;
        }
    }

    public static void keybind(Canvas canvas) {
        canvas.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                    p1D = -1;
                    break;
                case S:
                    p1D = 1;
                    break;
                case I:
                    p2D = -1;
                    break;
                case K:
                    p2D = 1;
                    break;
            }
        });
        canvas.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W:
                    if (p1D == -1)
                        p1D = 0;
                    break;
                case S:
                    if (p1D == 1)
                        p1D = 0;
                    break;
                case I:
                    if (p2D == -1)
                        p2D = 0;
                    break;
                case K:
                    if (p2D == 1)
                        p2D = 0;
                    break;
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ball = new Stage();
        p1 = new Stage();
        p2 = new Stage();

        p1X = 0;
        p1Y = 0;
        p2X = WIDTH - PADDLE_WIDTH;
        p2Y = HEIGHT - PADDLE_HEIGHT;
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;

        initializeStage(p1, PADDLE_WIDTH, PADDLE_HEIGHT);
        initializeStage(p2, PADDLE_WIDTH, PADDLE_HEIGHT);
        initializeStage(ball, BALL_SIZE, BALL_SIZE);

        score = new Stage();
        score.setResizable(false);
        score.setAlwaysOnTop(true);
        score.initStyle(StageStyle.TRANSPARENT);
        Group root = new Group();
        Scene s = new Scene(root, SCORE_WIDTH, SCORE_HEIGHT, Color.rgb(0, 0, 0, 0));
        Canvas canvas = new Canvas(SCORE_WIDTH, SCORE_HEIGHT);
        keybind(canvas);
        scoreGC = canvas.getGraphicsContext2D();
        scoreGC.setFill(theme);
        scoreGC.setTextBaseline(VPos.CENTER);
        scoreGC.setFont(new Font(SCORE_HEIGHT * 0.95));
        moveStage(score, WIDTH / 2 - SCORE_WIDTH / 2, 0);

        root.getChildren().add(canvas);
        canvas.setFocusTraversable(true);
        score.toFront();
        score.setScene(s);
        score.show();

        root = new Group();
        s = new Scene(root, BALL_SIZE, BALL_SIZE, theme);
        canvas = new Canvas(0, 0);
        root.getChildren().add(canvas);
        canvas.setFocusTraversable(true);
        keybind(canvas);

        ball.setScene(s);

        AnimationTimer timer = new AnimationTimer() {
            long lt = 0;

            @Override
            public void handle(long t) {

                p1Y += p1D * PADDLE_SPEED;
                p2Y += p2D * PADDLE_SPEED;

                if (p1Y < 0)
                    p1Y = 0;
                if (p1Y > HEIGHT - PADDLE_HEIGHT)
                    p1Y = HEIGHT - PADDLE_HEIGHT;

                if (p2Y < 0)
                    p2Y = 0;
                if (p2Y > HEIGHT - PADDLE_HEIGHT)
                    p2Y = HEIGHT - PADDLE_HEIGHT;

                ballX += Math.cos(ballDir) * ballSpeed;
                ballY += Math.sin(ballDir) * ballSpeed;

                if (ballY < 0) {
                    ballDir = 2 * Math.PI - ballDir;
                    ballY = 0;
                }
                if (ballY > HEIGHT - BALL_SIZE) {
                    ballDir = 2 * Math.PI - ballDir;
                    ballY = HEIGHT - BALL_SIZE;
                }
                if (ballX < PADDLE_WIDTH) {
                    if (ballY + BALL_SIZE > p1Y && ballY < p1Y + PADDLE_HEIGHT) {
                        ballDir = Math.PI - ballDir;
                        ballX = p1X + PADDLE_WIDTH;
                        ballSpeed += ballAccel;
                    }
                }
                if (ballX + BALL_SIZE > WIDTH - PADDLE_WIDTH) {
                    if (ballY + BALL_SIZE > p2Y && ballY < p2Y + PADDLE_HEIGHT) {
                        ballDir = Math.PI - ballDir;
                        ballX = p2X - BALL_SIZE;
                        ballSpeed += ballAccel;
                    }
                }
                if (ballX < 0) {
                    p2S++;
                    reset(false);
                }
                if (ballX + BALL_SIZE > WIDTH) {
                    p1S++;
                    reset(true);
                }

                moveStage(p1, p1X, p1Y);
                moveStage(p2, p2X, p2Y);
                moveStage(ball, ballX, ballY);

                scoreGC.clearRect(0, 0, SCORE_WIDTH, SCORE_HEIGHT);
                scoreGC.setTextAlign(TextAlignment.LEFT);
                scoreGC.fillText("" + p1S, 5, SCORE_HEIGHT / 2);

                scoreGC.setTextAlign(TextAlignment.RIGHT);
                scoreGC.fillText("" + p2S, SCORE_WIDTH - 5, SCORE_HEIGHT / 2);

            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}