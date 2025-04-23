package org.example.recapproject12.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdService {

    public String randomID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
