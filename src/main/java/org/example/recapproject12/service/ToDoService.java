package org.example.recapproject12.service;

import org.example.recapproject12.dto.ToDoDTO;
import org.example.recapproject12.enums.Status;
import org.example.recapproject12.exceptions.IdNotFound;
import org.example.recapproject12.exceptions.MissingDataToConstructToDo;
import org.example.recapproject12.model.ToDo;
import org.example.recapproject12.repository.ToDoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Service
public class ToDoService {


    private final ToDoRepository toDoRepository;
    private final IdService idService;

    public ToDoService(ToDoRepository toDoRepository, IdService idService) {
        this.toDoRepository = toDoRepository;
        this.idService = idService;
    }

    public List<ToDo> getAll() {
        return toDoRepository.findAll();
    }

    public ToDo getByID(String id) {
        ToDo toDoWithId = toDoRepository.findById(id).orElse(null);
        if(toDoWithId == null) {
            throw new IdNotFound("No To-Do with id " + id + " found.");
        } else {
            return toDoWithId;
        }
    }

    public ToDo addToDo(ToDoDTO toDoDTO) {
        if (toDoDTO.getDescription() != null) {
            ToDo toDo = new ToDo(idService.randomID(), toDoDTO.getDescription(), toDoDTO.getStatus());
            return toDoRepository.save(toDo);
        } else {
            throw new MissingDataToConstructToDo("""
                    No To-do could be added. Please add a description:
                    {
                        "description": "Your description here",
                        "status": "OPEN"
                    }
                    """);
        }


    }

    public ToDo updateToDo(String id, ToDoDTO toDoDTO) {
        ToDo old = toDoRepository.findById(id).orElse(null);
        if (old != null) {
            if (toDoDTO.getDescription() != null) {
                old = old.withDescription(toDoDTO.getDescription());
            }
            if (toDoDTO.getStatus() != null) {
                old = old.withStatus(toDoDTO.getStatus());
            }
            return toDoRepository.save(old);
        }
        else {
            throw new IdNotFound("No To-Do with id " + id + " found.");
        }
    }

    public ToDo deleteById(String id) {
        ToDo toBeDeleted = toDoRepository.findById(id).orElse(null);
        if (toBeDeleted != null) {
            toDoRepository.deleteById(id);
            return toBeDeleted;
        }
        throw new IdNotFound("No To-Do with id " + id + " found.");
    }
}
