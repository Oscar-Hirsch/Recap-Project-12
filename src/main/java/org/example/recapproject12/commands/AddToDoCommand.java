package org.example.recapproject12.commands;

import org.example.recapproject12.dto.ToDoDTO;
import org.example.recapproject12.exceptions.ChatGPTError;
import org.example.recapproject12.exceptions.IdNotFound;
import org.example.recapproject12.exceptions.MissingDataToConstructToDo;
import org.example.recapproject12.model.ToDo;
import org.example.recapproject12.repository.ToDoRepository;
import org.example.recapproject12.service.GPTService;
import org.example.recapproject12.service.IdService;

public class AddToDoCommand implements Command {
    private final ToDoRepository toDoRepository;
    private final ToDoDTO toDoDTO;
    private final GPTService gptService;
    private final IdService idService;
    private String randomID;

    public AddToDoCommand(ToDoRepository repository, ToDoDTO toDoDTO, GPTService gptService, IdService idService) {
        this.toDoRepository = repository;
        this.toDoDTO = toDoDTO;
        this.gptService = gptService;
        this.idService = idService;
    }

    @Override
    public void execute() {
        if (toDoDTO.getDescription() != null && toDoDTO.getDescription().strip() != "") {
            try {
                this.randomID = idService.randomID();
                String correctedDescription = gptService.correctGrammar(toDoDTO.getDescription());
                ToDo toDo = new ToDo(this.randomID, correctedDescription, toDoDTO.getStatus());
                toDoRepository.save(toDo);
            } catch (Exception e) {
                throw new ChatGPTError("AI is conscious and busy taking over the world. Please try again later.");
            }

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

    @Override
    public void undo() {
        ToDo toBeDeleted = toDoRepository.findById(this.randomID).orElse(null);
        if (toBeDeleted != null) {
            toDoRepository.deleteById(this.randomID);
        } else {
            throw new IdNotFound("No To-Do with id " + this.randomID + " found.");
        }
    }
}
