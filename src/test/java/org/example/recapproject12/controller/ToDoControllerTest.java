package org.example.recapproject12.controller;

import com.jayway.jsonpath.JsonPath;
import org.example.recapproject12.enums.Status;
import org.example.recapproject12.model.ToDo;
import org.example.recapproject12.repository.ToDoRepository;
import org.example.recapproject12.service.GPTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
@TestPropertySource(properties = {
        "OpenAI_AuthKey=dummy-test-key"
})
class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoRepository repository;

   @Autowired
   private MockRestServiceServer mockRestServiceServer;


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
                .andExpect(status().isOk())
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
    void getAll_returnsNoContent_WhenCalledOnEmptyDatabase() throws Exception {
        repository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getByID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/2"))
                .andExpect(status().isOk())
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
    void getByID_returnsNotFoundException_WhenCalledWithInvalidID() throws Exception {
        repository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/23423"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteByID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/2"))
                .andExpect(status().isOk())
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
    void deleteByID_throwsNoFoundException_WhenCalledWithInvalidId() throws Exception {
        repository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/2003"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addToDo() throws Exception{
        mockRestServiceServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                        {
                          "id": "chatcmpl-B9MBs8CjcvOU2jLn4n570S5qMJKcT",
                          "object": "chat.completion",
                          "created": 1741569952,
                          "model": "gpt-4.1-2025-04-14",
                          "choices": [
                            {
                              "index": 0,
                              "message": {
                                "role": "assistant",
                                "content": "Another last task",
                                "refusal": null,
                                "annotations": []
                              },
                              "logprobs": null,
                              "finish_reason": "stop"
                            }
                          ],
                          "usage": {
                            "prompt_tokens": 19,
                            "completion_tokens": 10,
                            "total_tokens": 29,
                            "prompt_tokens_details": {
                              "cached_tokens": 0,
                              "audio_tokens": 0
                            },
                            "completion_tokens_details": {
                              "reasoning_tokens": 0,
                              "audio_tokens": 0,
                              "accepted_prediction_tokens": 0,
                              "rejected_prediction_tokens": 0
                            }
                          },
                          "service_tier": "default"
                        }
                        """, MediaType.APPLICATION_JSON));



        String result = mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "description": "Anothr ladst Task",
                            "status": "DONE"
                        }
                        """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String generatedId = JsonPath.read(result, "$.id");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/%s".formatted(generatedId)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("""
                        {
                            "id": "%s",
                            "description": "Another last task",
                            "status": "DONE"
                        }
                      """.formatted(generatedId)));
    }

    @Test
    void addToDo_throwsMissingDataException_whenCalledWithoutDescriptionField() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                            "status": "DONE"
                            }
                         """))
                .andExpect(status().isBadRequest());
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
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("""
                        {
                            "id": "2",
                            "description": "Infinite Task",
                            "status": "IN_PROGRESS"
                        }
                      """));
    }

    @Test
    void updateToDo_ThrowsIdNotFoundException_WhenCalledWithInvalidID() throws Exception{
        repository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                 "status": "OPEN"
                                 }
                                 """))
                .andExpect(status().isNotFound());
    }

}
