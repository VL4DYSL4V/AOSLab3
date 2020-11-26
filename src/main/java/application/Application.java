package application;

import entity.Register;
import entity.Soprocessor;
import util.IEEE754Converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Application {

    public void start() {
        sayHello();
        Register register = new Register("test", 5, 5);
        register.setValue("0 11110 1 11111");
        System.out.println("Max value: " + register.getValuePresentation() +
                " " + IEEE754Converter.decode(register.getValuePresentation(), register.getCharacteristicLength()));
        register.setValue("0 00001 1 00000");
        System.out.println("Min value: " + register.getValuePresentation() +
                " " + IEEE754Converter.decode(register.getValuePresentation(), register.getCharacteristicLength()));
        register.setValue("1 00001 1 00000");
        System.out.println("Min negative value: " + register.getValuePresentation() +
                " " + IEEE754Converter.decode(register.getValuePresentation(), register.getCharacteristicLength()));
        register.setValue("1 00000 0 00000");
        System.out.println("+1,0E0: " + register.getValuePresentation() +
                " " + IEEE754Converter.decode(register.getValuePresentation(), register.getCharacteristicLength()));
        register.setValue("0 11111 1 00000");
        System.out.println("+Infinity: " + register.getValuePresentation() +
                " " + IEEE754Converter.decode(register.getValuePresentation(), register.getCharacteristicLength()));
        register.setValue("1 11111 1 00000");
        System.out.println("-Infinity: " + register.getValuePresentation() +
                " " + IEEE754Converter.decode(register.getValuePresentation(), register.getCharacteristicLength()));
        register.setValue("0 00000 1 00100");
        System.out.println("NaN: " + register.getValuePresentation() +
                " " + IEEE754Converter.decode(register.getValuePresentation(), register.getCharacteristicLength()));
        register.setValue("0 00100 0 00100");
        System.out.println("Denormalized: " + register.getValuePresentation() +
                " " + IEEE754Converter.decode(register.getValuePresentation(), register.getCharacteristicLength()));
        Map<String, Double> input = askXY();
        System.out.println(input);
        Soprocessor soprocessor = new Soprocessor(5, 5);
        soprocessor.calculateFormula(input.get("x"), input.get("y"));
        System.out.println(soprocessor);
    }

    private void sayHello() {
        System.out.println("Lab-3 AOS, Arzamastsev Vladislav K-23");
    }

    private Map<String, Double> askXY() {
        Map<String, Double> out = new HashMap<>();
        Scanner sc = new Scanner(System.in);
        while (!out.containsKey("x")) {
            System.out.println("Input x: ");
            try {
                double d = Double.parseDouble(sc.nextLine());
                if (d < 0) {
                    System.out.println("X must be positive");
                } else {
                    out.put("x", d);
                }
            } catch (NumberFormatException exception) {
                System.out.println("Invalid number format");
            }
        }
        while (!out.containsKey("y")) {
            System.out.println("Input y: ");
            try {
                out.put("y", Double.valueOf(sc.nextLine()));
            } catch (NumberFormatException exception) {
                System.out.println("Invalid number format");
            }
        }
        return out;
    }
}
