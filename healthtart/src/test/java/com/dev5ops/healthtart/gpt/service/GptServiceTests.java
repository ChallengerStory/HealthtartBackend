package com.dev5ops.healthtart.gpt.service;

import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GptServiceTests {

    @Autowired
    private GptService gptService;

    @Autowired
    private UserService userService;

    @Test
    public void testCallOpenAI() throws JsonProcessingException {
        String userCode = "20241007-05bfb06b-8eda-4857-8681-40d1eccb829d";
        UserDTO user = userService.findById(userCode);
        System.out.println("유저 정보 = " + user.getUserName());

        String bodyPart = "하체";
        int workoutTime = 90;
        String response = gptService.generateRoutine(userCode, bodyPart, workoutTime);
        System.out.println("GPT 응답 = " + response);
        assertNotNull(response);
    }

}