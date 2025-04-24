package org.example.recapproject12.controller;

import com.jayway.jsonpath.JsonPath;
import org.example.recapproject12.enums.Status;
import org.example.recapproject12.model.ToDo;
import org.example.recapproject12.repository.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class toDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        ToDo todo = new ToDo("1","One Task", Status.OPEN);
        ToDo todo2 = new ToDo("2","Second Task", Status.IN_PROGRESS);
        ToDo todo3 = new ToDo("3", "Third Task", Status.DONE);
        List<ToDo> database = List.of(todo, todo2, todo3);
        repository.saveAll(database);

    }


    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [{
                        "id": "1",
                        "description": "One Task",
                        "status": "OPEN"
                        },
                        {
                        "id": "2",
                        "description": "Second Task",
                        "status": "IN_PROGRESS"
                        },
                        {
                        "id": "3",
                        "description": "Third Task",
                        "status": "DONE"
                        }]
                        """));
    }

    @Test
    void getByID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("""
                        {
                            "id": "2",
                            "description": "Second Task",
                            "status": "IN_PROGRESS"
                        }
                      """));
    }

    @Test
    void deleteByID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("""
                        {
                            "id": "2",
                            "description": "Second Task",
                            "status": "IN_PROGRESS"
                        }
                      """));
    }

    @Test
    void addToDo() throws Exception{
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "description": "Another last Task",
                            "status": "DONE"
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        String generatedId = JsonPath.read(result, "$.id");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/%s".formatted(generatedId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("""
                        {
                            "id": "%s",
                            "description": "Another last Task",
                            "status": "DONE"
                        }
                      """.formatted(generatedId)));
    }

    @Test
    void updateToDo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "description": "Infinite Task"
                        }
                      """))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("""
                        {
                            "id": "2",
                            "description": "Infinite Task",
                            "status": "IN_PROGRESS"
                        }
                      """));
    }

}
