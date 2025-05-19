package MonteCarloPI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class MonteCarloVisualizer extends Application {
    private static final int CANVAS_SIZE = 600;
    private List<MonteCarloPi.Point> samplePoints = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane root = new StackPane(canvas);

        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
        gc.setFill(Color.BLUE);
        gc.fillOval(0, 0, CANVAS_SIZE, CANVAS_SIZE);

        Thread simulationThread = new Thread(() -> {
            try {
                double piEstimate = MonteCarloPi.estimatePiWithThreads(MonteCarloPi.NUM_POINTS,
                        MonteCarloPi.NUM_THREADS, samplePoints);
                javafx.application.Platform.runLater(() -> updateDisplay(gc, samplePoints, piEstimate));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        simulationThread.setDaemon(true);
        simulationThread.start();

        Scene scene = new Scene(root, CANVAS_SIZE, CANVAS_SIZE + 20);
        stage.setScene(scene);
        stage.setTitle("Monte Carlo π Visualization");
        stage.show();
    }

    private void updateDisplay(GraphicsContext gc, List<MonteCarloPi.Point> points, double piEstimate) {
        gc.clearRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
        gc.setFill(Color.BLUE);
        gc.fillOval(0, 0, CANVAS_SIZE, CANVAS_SIZE);

        for (MonteCarloPi.Point p : points) {
            double x = (p.x + 1) * (CANVAS_SIZE / 2);
            double y = (p.y + 1) * (CANVAS_SIZE / 2);
            gc.setFill(p.insideCircle ? Color.GREEN : Color.RED);
            gc.fillOval(x, y, 2, 2);
        }

        gc.setFill(Color.BLACK);
        gc.fillText("Estimated π: " + String.format("%.5f", piEstimate), 10, CANVAS_SIZE + 15);
    }

    public static void main(String[] args) {
        launch(args);
    }
}