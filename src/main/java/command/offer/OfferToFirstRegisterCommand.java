package command.offer;

import entity.Register;
import util.IEEE754Converter;

import java.util.List;
import java.util.Objects;

public class OfferToFirstRegisterCommand implements OfferCommand {

    private final List<Register> registers;

    public OfferToFirstRegisterCommand(List<Register> registers) {
        this.registers = registers;
    }

    @Override
    public void offer(double value) {
        if(registers == null || registers.isEmpty()){
            return;
        }
        for (int i = registers.size() - 1; i > 0; i--) {
            registers.get(i).setValue(registers.get(i - 1).getValuePresentation());
        }
        Register zero = registers.get(0);
        zero.setValue(IEEE754Converter.encode(value, zero.getCharacteristicLength(), zero.getMantissaLength()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfferToFirstRegisterCommand that = (OfferToFirstRegisterCommand) o;
        return Objects.equals(registers, that.registers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registers);
    }

    @Override
    public String toString() {
        return "OfferToFirstRegisterCommand";
    }
}
