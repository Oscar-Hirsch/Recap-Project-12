package org.example.recapproject12.invoker;

import org.example.recapproject12.commands.Command;
import org.example.recapproject12.repository.ToDoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Component
public class Invoker {
    private Stack<Command> undoStack = new Stack<>();

    public void execute(Command command) {
        command.execute();
        undoStack.push(command);
    }

    public void undo() {
        undoStack.pop().undo();
    }
}
