package org.example.recapproject12.controller;


import org.example.recapproject12.dto.ToDoDTO;
import org.example.recapproject12.model.ToDo;
import org.example.recapproject12.service.ToDoService;
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
    public List<ToDo> getAll() {
        return toDoService.getAll();
    }

    @GetMapping("/{id}")
    public ToDo getByID(@PathVariable String id) {
        return toDoService.getByID(id);
    }

    @PostMapping
    public ToDo addToDo(@RequestBody ToDoDTO toDoDTO) {
        return toDoService.addToDo(toDoDTO);
    }

    @PutMapping("/{id}")
    public ToDo updateToDo(@PathVariable String id, @RequestBody ToDoDTO toDoDTO) {
        return toDoService.updateToDo(id, toDoDTO);
    }



}
