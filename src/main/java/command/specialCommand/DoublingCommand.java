package command.specialCommand;

import entity.Register;

import java.util.List;
import java.util.Objects;

public class DoublingCommand implements Command {

    private final List<Register> registers;

    public DoublingCommand(List<Register> registers) {
        this.registers = registers;
    }

    @Override
    public void execute() {
        if(registers == null || registers.isEmpty()){
            return;
        }
        for (int i = registers.size() - 1; i > 0 ; i--) {
            registers.get(i).setValue(registers.get(i - 1).getValuePresentation());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoublingCommand that = (DoublingCommand) o;
        return Objects.equals(registers, that.registers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registers);
    }

    @Override
    public String toString() {
        return "DoublingCommand";
    }
}
