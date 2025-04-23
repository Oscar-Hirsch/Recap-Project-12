package org.example.recapproject12.dto;

import lombok.Builder;
import lombok.Data;
import org.example.recapproject12.enums.Status;

@Data
public class ToDoDTO {
    private String description;
    private Status status;

    public ToDoDTO(String description, Status status) {
        this.description = description;
        this.status = status;

    }
}
