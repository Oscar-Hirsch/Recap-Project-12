package org.example.recapproject12.commands;

import org.example.recapproject12.dto.ToDoDTO;
import org.example.recapproject12.exceptions.IdNotFound;
import org.example.recapproject12.model.ToDo;
import org.example.recapproject12.repository.ToDoRepository;

public class UpdateToDoCommand implements Command{
    private final ToDoRepository toDoRepository;
    private final String id;
    private final ToDoDTO toDoDTO;
    private ToDo toUpdate;

    public UpdateToDoCommand(ToDoRepository toDoRepository, String id, ToDoDTO toDoDTO) {
        this.id = id;
        this.toDoRepository = toDoRepository;
        this.toDoDTO = toDoDTO;
    }

    @Override
    public void execute() {
        this.toUpdate = toDoRepository.findById(id).orElse(null);
        ToDo updated = this.toUpdate;
        if (updated != null) {
            if (toDoDTO.getDescription() != null) {
                updated = updated.withDescription(toDoDTO.getDescription());
            }
            if (toDoDTO.getStatus() != null) {
                updated = updated.withStatus(toDoDTO.getStatus());
            }
            toDoRepository.save(updated);
        }
        else {
            throw new IdNotFound("No To-Do with id " + id + " found.");
        }

    }

    @Override
    public void undo() {
        if (toUpdate != null) {
            toDoRepository.save(toUpdate);
        } else {
            throw new IdNotFound("Cannot undo To-Do with id " + id);
        }

    }
}
