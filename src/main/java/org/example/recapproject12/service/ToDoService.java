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
        if (old != null) {
            System.out.println(old);
            if (toDoDTO.getDescription() != null) {
                old = old.withDescription(toDoDTO.getDescription());
            }
            if (toDoDTO.getStatus() != null) {
                old = old.withStatus(toDoDTO.getStatus());
            }
            return toDoRepository.save(old);
        }
        return null;
    }

    public ToDo deleteById(String id) {
        ToDo toBeDeleted = toDoRepository.findById(id).orElse(null);
        if (toBeDeleted != null) {
            toDoRepository.deleteById(id);
            return toBeDeleted;
        }
        return null;
    }
}
