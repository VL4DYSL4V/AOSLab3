package command.mathCommand;

import entity.Register;
import util.IEEE754Converter;

import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class MathOnFirstExecutor implements UnaryMathCommand {
    private final List<Register> registers;

    public MathOnFirstExecutor(List<Register> registers) {
        this.registers = registers;
    }

    @Override
    public void execute(UnaryOperator<Double> whatToDo) {
        if(registers == null || registers.isEmpty()){
            return;
        }
        Register zero = registers.get(0);
        double value = IEEE754Converter.decode(zero.getValuePresentation(),
                zero.getCharacteristicLength());
        double result = whatToDo.apply(value);
        String out = IEEE754Converter.encode(result, zero.getCharacteristicLength(), zero.getMantissaLength());
        zero.setValue(out);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MathOnFirstExecutor that = (MathOnFirstExecutor) o;
        return Objects.equals(registers, that.registers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registers);
    }

    @Override
    public String toString() {
        return "MathOnFirstExecutor";
    }
}
