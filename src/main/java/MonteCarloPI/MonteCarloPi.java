package MonteCarloPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class MonteCarloPi {
    static final long NUM_POINTS = 50_000_000L;
    static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int SAMPLE_SIZE = 10_000; // Points for visualization

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long startTimeSingle = System.nanoTime();
        double piSingle = estimatePiWithoutThreads(NUM_POINTS);
        long endTimeSingle = System.nanoTime();
        System.out.printf("Single-threaded π: %.5f, Time: %d ms%n", piSingle, (endTimeSingle - startTimeSingle) / 1_000_000);

        List<Point> samplePoints = new ArrayList<>();
        long startTimeMulti = System.nanoTime();
        double piMulti = estimatePiWithThreads(NUM_POINTS, NUM_THREADS, samplePoints);
        long endTimeMulti = System.nanoTime();
        System.out.printf("Multi-threaded π: %.5f, Time: %d ms%n", piMulti, (endTimeMulti - startTimeMulti) / 1_000_000);

        BenchmarkExporter.exportToCSV("monte_carlo_benchmark.csv", piSingle, piMulti,
                (endTimeSingle - startTimeSingle) / 1_000_000,
                (endTimeMulti - startTimeMulti) / 1_000_000);
    }

    public static double estimatePiWithoutThreads(long numPoints) {
        Random random = new Random();
        long pointsInsideCircle = 0;
        double radius = 1.0;

        for (long i = 0; i < numPoints; i++) {
            double x = (random.nextDouble() * 2 * radius) - radius;
            double y = (random.nextDouble() * 2 * radius) - radius;
            if (x * x + y * y <= radius * radius) {
                pointsInsideCircle++;
            }
        }
        return 4.0 * pointsInsideCircle / numPoints;
    }

    public static double estimatePiWithThreads(long numPoints, int numThreads, List<Point> samplePoints)
            throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        AtomicLong pointsInsideCircle = new AtomicLong(0);
        long pointsPerThread = numPoints / numThreads;
        double radius = 1.0;

        for (int i = 0; i < numThreads; i++) {
            final long pointsToProcess = (i == numThreads - 1) ? (numPoints - (pointsPerThread * (numThreads - 1))) : pointsPerThread;
            executor.submit(() -> {
                Random random = new Random();
                long localInsideCircle = 0;
                for (long j = 0; j < pointsToProcess; j++) {
                    double x = (random.nextDouble() * 2 * radius) - radius;
                    double y = (random.nextDouble() * 2 * radius) - radius;
                    boolean inside = x * x + y * y <= radius * radius;
                    if (inside) {
                        localInsideCircle++;
                    }
                    synchronized (samplePoints) {
                        if (samplePoints.size() < SAMPLE_SIZE) {
                            samplePoints.add(new Point(x, y, inside));
                        }
                    }
                }
                pointsInsideCircle.addAndGet(localInsideCircle);
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(10);
        }
        return 4.0 * pointsInsideCircle.get() / numPoints;
    }

    public static class Point {
        double x, y;
        boolean insideCircle;

        public Point(double x, double y, boolean insideCircle) {
            this.x = x;
            this.y = y;
            this.insideCircle = insideCircle;
        }
    }
}