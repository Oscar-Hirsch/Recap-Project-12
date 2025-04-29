package org.example.recapproject12.exceptions;

public class ChatGPTError extends RuntimeException {
    public ChatGPTError(String message) {
        super(message);
    }
}
