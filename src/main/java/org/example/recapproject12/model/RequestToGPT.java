package org.example.recapproject12.model;

import java.util.List;

public record RequestToGPT(String model, List<Message> messages) {
}
