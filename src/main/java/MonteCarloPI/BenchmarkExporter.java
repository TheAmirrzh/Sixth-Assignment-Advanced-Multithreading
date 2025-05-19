package MonteCarloPI;

import java.io.FileWriter;
import java.io.IOException;

public class BenchmarkExporter {
    public static void exportToCSV(String filename, double piSingle, double piMulti, long timeSingle, long timeMulti) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Method,Pi Estimate,Execution Time (ms)\n");
            writer.write(String.format("Single-threaded,%.5f,%d\n", piSingle, timeSingle));
            writer.write(String.format("Multi-threaded,%.5f,%d\n", piMulti, timeMulti));
        } catch (IOException e) {
            System.err.println("Error exporting benchmark: " + e.getMessage());
        }
    }
}