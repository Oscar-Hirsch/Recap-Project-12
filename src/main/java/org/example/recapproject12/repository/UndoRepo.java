package org.example.recapproject12.repository;
import org.example.recapproject12.model.ToDo;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoRepo extends AbstractUndoableEdit {

    private final ToDoRepository repository;
    private final ToDo todo;

    @Override
    public void undo() {
        super.undo();

    }

}
