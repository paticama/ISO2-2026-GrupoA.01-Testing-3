package iso2.irrigation.ui;

import iso2.irrigation.domain.IrrigationAdvisor;
import iso2.irrigation.domain.SensorReading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Command-line user interface. Only reads input / writes output;
 * all decisions are delegated to IrrigationAdvisor.
 * Input and output are injected so they can be tested.
 */
public class ConsoleApp {

    private final BufferedReader in;
    private final PrintStream out;
    private final IrrigationAdvisor advisor;

    public ConsoleApp(BufferedReader in, PrintStream out, IrrigationAdvisor advisor) {
        this.in = in;
        this.out = out;
        this.advisor = advisor;
    }

    /** Asks for the data once and prints the recommendation (or the error). */
    public void run() throws IOException {
        try {
            double moisture = readDouble("Soil moisture (%): ");
            double temperature = readDouble("Temperature (C): ");
            boolean rain = readRain("Rain forecast in next 24h? (y/n): ");
            LocalTime time = readTime("Current time (HH:mm): ");

            SensorReading reading = new SensorReading(moisture, temperature, rain, time);
            out.println("Recommendation: " + advisor.recommend(reading).getDescription());
        } catch (IllegalArgumentException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private String ask(String prompt) throws IOException {
        out.print(prompt);
        String line = in.readLine();
        if (line == null) {
            throw new IllegalArgumentException("Unexpected end of input");
        }
        return line.trim();
    }

    private double readDouble(String prompt) throws IOException {
        String text = ask(prompt);
        try {
            return Double.parseDouble(text.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + text + "' is not a valid number");
        }
    }

    private boolean readRain(String prompt) throws IOException {
        String text = ask(prompt).toLowerCase();
        if (text.equals("y") || text.equals("yes")) {
            return true;
        }
        if (text.equals("n") || text.equals("no")) {
            return false;
        }
        throw new IllegalArgumentException("Answer must be y or n");
    }

    private LocalTime readTime(String prompt) throws IOException {
        String text = ask(prompt);
        try {
            return LocalTime.parse(text);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("'" + text + "' is not a valid time (use HH:mm)");
        }
    }
}
