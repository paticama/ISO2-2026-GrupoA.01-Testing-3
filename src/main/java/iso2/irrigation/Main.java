package iso2.irrigation;

import iso2.irrigation.domain.IrrigationAdvisor;
import iso2.irrigation.ui.ConsoleApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        new ConsoleApp(in, System.out, new IrrigationAdvisor()).run();
    }
}
