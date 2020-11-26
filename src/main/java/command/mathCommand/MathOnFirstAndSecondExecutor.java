package command.mathCommand;

import entity.Register;
import util.IEEE754Converter;

import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;

public class MathOnFirstAndSecondExecutor implements BinaryMathCommand {

    private final List<Register> registers;

    public MathOnFirstAndSecondExecutor(List<Register> registers) {
        this.registers = registers;
    }

    @Override
    public void execute(BinaryOperator<Double> whatToDo) {
        if(registers == null || registers.size() < 2){
            return;
        }
        Register zero = registers.get(0);
        Register first = registers.get(1);
        double valueZero = IEEE754Converter.decode(zero.getValuePresentation(),
                zero.getCharacteristicLength());
        double valueFirst = IEEE754Converter.decode(first.getValuePresentation(),
                first.getCharacteristicLength());
        double result = whatToDo.apply(valueZero, valueFirst);
        String out = IEEE754Converter.encode(result, zero.getCharacteristicLength(), zero.getMantissaLength());
        for (int i = 1; i < registers.size() - 1; i++){
            registers.get(i).setValue(registers.get(i + 1).getValuePresentation());
        }
        registers.get(registers.size() - 1).setValue("0");
        zero.setValue(out);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MathOnFirstAndSecondExecutor that = (MathOnFirstAndSecondExecutor) o;
        return Objects.equals(registers, that.registers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registers);
    }

    @Override
    public String toString() {
        return "MathOnFirstAndSecondExecutor";
    }
}
