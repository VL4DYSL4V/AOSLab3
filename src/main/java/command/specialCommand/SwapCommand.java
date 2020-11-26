package command.specialCommand;

import entity.Register;

import java.util.List;
import java.util.Objects;

public class SwapCommand implements Command {

    private final List<Register> registers;

    public SwapCommand(List<Register> registers) {
        this.registers = registers;
    }

    @Override
    public void execute() {
        if(registers == null || registers.size() < 2){
            return;
        }
        String tmp = registers.get(0).getValuePresentation();
        registers.get(0).setValue(registers.get(1).getValuePresentation());
        registers.get(1).setValue(tmp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwapCommand that = (SwapCommand) o;
        return Objects.equals(registers, that.registers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registers);
    }

    @Override
    public String toString() {
        return "SwapCommand";
    }
}
