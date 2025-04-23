package org.example.recapproject12.service;

import org.example.recapproject12.dto.ToDoDTO;
import org.example.recapproject12.model.ToDo;
import org.example.recapproject12.repository.ToDoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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
        return toDoRepository.findById(id).orElse(null);
    }

    public ToDo addToDo(ToDoDTO toDoDTO) {
        ToDo toDo = new ToDo(idService.randomID(), toDoDTO.getDescription(), toDoDTO.getStatus());
        return toDoRepository.save(toDo);
    }

    public ToDo updateToDo(String id, ToDoDTO toDoDTO) {
        ToDo old = toDoRepository.findById(id).orElse(null);
        ToDo exisitingToDo = old;
        if (old != null) {
            if (toDoDTO.getDescription() != null) {
                exisitingToDo = exisitingToDo.withDescription(toDoDTO.getDescription());
            }
            if (toDoDTO.getStatus() != null) {
                exisitingToDo = exisitingToDo.withStatus(toDoDTO.getStatus());
            }
            return toDoRepository.save(exisitingToDo);
        }
        return null;
    }
}
