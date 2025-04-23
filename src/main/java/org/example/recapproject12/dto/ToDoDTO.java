package org.example.recapproject12.dto;

import lombok.Builder;
import lombok.Data;
import org.example.recapproject12.enums.Status;

@Data
@Builder
public class ToDoDTO {
    private String description;
    private Status status;
}
