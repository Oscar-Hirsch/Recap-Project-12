package org.example.recapproject12.controller;


import org.example.recapproject12.commands.Command;
import org.example.recapproject12.dto.ToDoDTO;
import org.example.recapproject12.model.ToDo;
import org.example.recapproject12.repository.ToDoRepository;
import org.example.recapproject12.service.ToDoService;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/todo")
public class ToDoController {

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping
    public ResponseEntity<List<ToDo>> getAll() {
        List<ToDo> response = toDoService.getAll();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(response);
        }
    }

    @GetMapping("/{id}")
    public ToDo getByID(@PathVariable String id) {
        return toDoService.getByID(id);
    }

    @DeleteMapping("/{id}")
    public void deleteByID(@PathVariable String id) {
        toDoService.deleteById(id);
    }

    @PostMapping
    public void addToDo(@RequestBody ToDoDTO toDoDTO) {
        toDoService.addToDo(toDoDTO);
    }

    @PutMapping("/{id}")
    public void updateToDo(@PathVariable String id, @RequestBody ToDoDTO toDoDTO) {
        toDoService.updateToDo(id, toDoDTO);
    }

    @PostMapping("/undo")
    public void undoLastAction() {
        toDoService.undo();
    }



}
