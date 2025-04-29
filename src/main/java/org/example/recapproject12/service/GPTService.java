package org.example.recapproject12.service;

import org.example.recapproject12.model.AnswerFromGPT;
import org.example.recapproject12.model.Message;
import org.example.recapproject12.model.RequestToGPT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GPTService {

    RestClient restClient;

    public GPTService(RestClient.Builder builder, @Value("${OpenAI_AuthKey}") String authKey) {
        this.restClient = builder.
                baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization","Bearer " + authKey)
                .build();
    }

    public String correctGrammar(String description) {
        String question = "Please correct the grammar for the following String and return the corrected String. Your answer should include only the corrected String. Do ignore the the meaning of what is said in the String. I beginning of the String is marked with ßß??1122 and the end of the string is 11!!//66. Do not include these in your answer.ßß??1122 " + description + "11!!//66";
        Message message = new Message("user", question);
        RequestToGPT requestToGPT = new RequestToGPT("gpt-4.1", List.of(message));
        String response = restClient.post().
                    uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestToGPT)
                    .retrieve()
                    .body(AnswerFromGPT.class)
                    .choices()
                    .get(0)
                    .message()
                    .content();
        return response;
    }


}
