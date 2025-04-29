package org.example.recapproject12.service;

import org.example.recapproject12.commands.AddToDoCommand;
import org.example.recapproject12.commands.DeleteByIDCommand;
import org.example.recapproject12.commands.UpdateToDoCommand;
import org.example.recapproject12.dto.ToDoDTO;
import org.example.recapproject12.enums.Status;
import org.example.recapproject12.exceptions.ChatGPTError;
import org.example.recapproject12.exceptions.IdNotFound;
import org.example.recapproject12.exceptions.MissingDataToConstructToDo;
import org.example.recapproject12.invoker.Invoker;
import org.example.recapproject12.model.ToDo;
import org.example.recapproject12.repository.ToDoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private Invoker invoker;
    private final IdService idService;
    private final GPTService gptService;

    public ToDoService(ToDoRepository toDoRepository, IdService idService, GPTService gptService, Invoker invoker) {
        this.toDoRepository = toDoRepository;
        this.invoker = invoker;
        this.idService = idService;
        this.gptService = gptService;
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

    public void addToDo(ToDoDTO toDoDTO) {
        invoker.execute(new AddToDoCommand(toDoRepository, toDoDTO, gptService, idService));
    }

    public void updateToDo(String id, ToDoDTO toDoDTO) {
        invoker.execute(new UpdateToDoCommand(toDoRepository, id, toDoDTO));
    }

    public void deleteById(String id) {
        invoker.execute(new DeleteByIDCommand(toDoRepository, id));
    }

    public void undo() {
        invoker.undo();
    }
}
