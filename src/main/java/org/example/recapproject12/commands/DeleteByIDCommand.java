package org.example.recapproject12.commands;

import org.example.recapproject12.exceptions.IdNotFound;
import org.example.recapproject12.model.ToDo;
import org.example.recapproject12.repository.ToDoRepository;

public class DeleteByIDCommand implements Command {
    private final ToDoRepository toDoRepository;
    private final String id;
    private ToDo toBeDeleted;

    public DeleteByIDCommand(ToDoRepository toDoRepository, String id) {
        this.id = id;
        this.toDoRepository = toDoRepository;
    }


    @Override
    public void execute() {
        this.toBeDeleted = toDoRepository.findById(id).orElse(null);
        if (toBeDeleted != null) {
            toDoRepository.deleteById(id);
        }
        throw new IdNotFound("No To-Do with id " + id + " found.");
    }

    @Override
    public void undo() {
        if(toBeDeleted != null) {
            toDoRepository.save(toBeDeleted);
        }
    }
}
