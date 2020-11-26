package command.mathCommand;

import java.util.function.BinaryOperator;

public interface BinaryMathCommand {

    void execute(BinaryOperator<Double> whatToDo);
}
