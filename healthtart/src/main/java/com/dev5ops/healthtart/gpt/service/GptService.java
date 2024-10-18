package com.dev5ops.healthtart.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface GptService {
    String generateRoutine(String userCode, String bodyPart, int time);

    void processRoutine(String response) throws JsonProcessingException;
}
