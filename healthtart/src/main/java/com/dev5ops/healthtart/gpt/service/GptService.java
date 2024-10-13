package com.dev5ops.healthtart.gpt.service;

public interface GptService {
    String generateRoutine(String userCode, String bodyPart, int time);
}
