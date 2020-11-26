package command.mathCommand;

import java.util.function.UnaryOperator;

public interface UnaryMathCommand {

    void execute(UnaryOperator<Double> whatToDo);
}
