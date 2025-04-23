package org.example.recapproject12.model;

import lombok.With;
import org.example.recapproject12.enums.Status;
import org.springframework.data.annotation.Id;

@With
public record ToDo(@Id String id, String description, Status status) {
}
