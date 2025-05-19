package Banking;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BankingVisualizer extends Application {
    private static Map<String, BankAccount> accounts;
    private ScheduledExecutorService executor;

    public static void launchVisualizer(Map<String, BankAccount> accounts) {
        BankingVisualizer.accounts = accounts;
        launch();
    }

    @Override
    public void start(Stage stage) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Account Balances Over Time");

        Map<String, XYChart.Series<Number, Number>> seriesMap = new HashMap<>();
        accounts.forEach((id, acc) -> {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(id);
            seriesMap.put(id, series);
            chart.getData().add(series);
        });

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            javafx.application.Platform.runLater(() -> {
                int time = (int) (System.currentTimeMillis() / 1000 % 60);
                seriesMap.forEach((id, series) -> {
                    double balance = accounts.get(id).getBalance();
                    series.getData().add(new XYChart.Data<>(time, balance));
                    if (series.getData().size() > 50) series.getData().remove(0);
                });
            });
        }, 0, 1, TimeUnit.SECONDS);

        Scene scene = new Scene(chart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Banking System Visualizer");
        stage.show();
    }

    @Override
    public void stop() {
        if (executor != null) executor.shutdown();
    }
}