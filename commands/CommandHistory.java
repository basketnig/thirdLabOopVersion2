package commands;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandHistory {
    private final Deque<Command> history = new ArrayDeque<>();

    public void push(Command command) {
        if (command != null) {
            history.push(command);
        }
    }

    public Command getLastCommand() {
        return history.peek();
    }

    public String undo() {
        if (history.isEmpty()) {
            return "история команд пуста, отменять нечего";
        }
        Command last = history.pop();
        return last.undo();
    }
}