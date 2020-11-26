package entity;

import command.mathCommand.BinaryMathCommand;
import command.mathCommand.MathOnFirstAndSecondExecutor;
import command.mathCommand.MathOnFirstExecutor;
import command.mathCommand.UnaryMathCommand;
import command.offer.OfferCommand;
import command.offer.OfferToFirstRegisterCommand;
import command.specialCommand.Command;
import command.specialCommand.DoublingCommand;
import command.specialCommand.SwapCommand;
import util.IEEE754Converter;

import java.util.*;

public class Soprocessor {

    private final List<Register> registers = new LinkedList<>();
    private final Map<String, OfferCommand> offerCommandMap = new HashMap<>();
    private final Map<String, Command> specialCommandMap = new HashMap<>();
    private final Map<String, UnaryMathCommand> unaryMathCommandMap = new HashMap<>();
    private final Map<String, BinaryMathCommand> binaryMathCommandMap = new HashMap<>();


    public Soprocessor(int characteristicsLength, int mantissaLength) {
        setupRegisters(characteristicsLength, mantissaLength);
        setupOfferCommandMap();
        setupSpecialCommandMap();
        setupUnaryMathCommandMap();
        setupBinaryMathCommandMap();
    }

    private void setupRegisters(int characteristicsLength, int mantissaLength) {
        registers.add(new Register("0", characteristicsLength, mantissaLength));
        registers.add(new Register("1", characteristicsLength, mantissaLength));
        registers.add(new Register("2", characteristicsLength, mantissaLength));
    }

    private void setupOfferCommandMap() {
        offerCommandMap.put("first", new OfferToFirstRegisterCommand(registers));
    }

    private void setupSpecialCommandMap() {
        specialCommandMap.put("double", new DoublingCommand(registers));
        specialCommandMap.put("swap", new SwapCommand(registers));
    }

    private void setupUnaryMathCommandMap() {
        unaryMathCommandMap.put("first", new MathOnFirstExecutor(registers));
    }

    private void setupBinaryMathCommandMap() {
        binaryMathCommandMap.put("firstAndSecond", new MathOnFirstAndSecondExecutor(registers));
    }

    // ln(x) * sin(y) + e^x
    public void calculateFormula(double x, double y) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Before");
        System.out.println(toString());
//        sc.next();
        offerCommandMap.get("first").offer(x);
        System.out.println("Offer x = " + x);
        System.out.println(toString());
//        sc.next();
        specialCommandMap.get("double").execute();
        System.out.println("Double x");
        System.out.println(toString());
//        sc.next();
        unaryMathCommandMap.get("first").execute(Math::log);
        System.out.println("log(x)");
        System.out.println(toString());
//        sc.next();
        offerCommandMap.get("first").offer(y);
        System.out.println("Offer y = " + y);
        System.out.println(toString());
//        sc.next();
        unaryMathCommandMap.get("first").execute(Math::sin);
        System.out.println("sin(y)");
        System.out.println(toString());
//        sc.next();
        binaryMathCommandMap.get("firstAndSecond").execute((a, b) -> a * b);
        System.out.println("x * y");
        System.out.println(toString());
//        sc.next();
        specialCommandMap.get("swap").execute();
        System.out.println("Swap");
        System.out.println(toString());
//        sc.next();
        unaryMathCommandMap.get("first").execute(Math::exp);
        System.out.println("e ^ x");
        System.out.println(toString());
//        sc.next();
        binaryMathCommandMap.get("firstAndSecond").execute(Double::sum);
        System.out.println("R1 + R2");
        System.out.println(toString());
        System.out.println("res = " + IEEE754Converter.decode(registers.get(0).getValuePresentation(),
                registers.get(0).getCharacteristicLength()));
        setZerosToRegisters();
    }

    private void setZerosToRegisters(){
        registers.forEach(r -> r.setValue("0"));
    }

    public List<Register> getRegisters() {
        return registers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Soprocessor{\n");
        for (Register register : registers) {
            sb.append("\t").append(register).append("\n");
        }
//        sb.append(", offerCommandMap = ").append(offerCommandMap).append("\n");
//        sb.append(", specialCommandMap = ").append(specialCommandMap).append("\n");
//        sb.append(", unaryMathCommandMap = ").append(unaryMathCommandMap).append("\n");
//        sb.append(", binaryMathCommandMap = ").append(binaryMathCommandMap);
        sb.append("}");
        return sb.toString();
    }
}
