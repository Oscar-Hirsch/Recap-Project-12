package org.example.recapproject12.repository;

import org.example.recapproject12.model.ToDo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends MongoRepository<ToDo, String> {

}
