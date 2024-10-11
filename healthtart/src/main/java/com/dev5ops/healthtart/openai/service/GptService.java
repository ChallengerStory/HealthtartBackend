package com.dev5ops.healthtart.openai.service;

import com.dev5ops.healthtart.common.config.GptConfig;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GptService {

    private GptConfig gptConfig;
    private RestTemplate restTemplate;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String callOpenAI(String prompt, int maxTokens) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptConfig.getSecretKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", gptConfig.getModel());
        requestBody.put("messages", new Object[] {
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });

        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", maxTokens);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }



    // 1. 프론트엔드에서 백엔드로 운동부위와 운동시간 정보 전송 (키, 몸무게, 성별, 나이는 회원가입할 때 받았으므로 DB에 존재)
    // 2. 백엔드에서는 요청이 들어오면 UserCode를 통해 키, 몸무게, 성별, 나이를 조회함

    // 프롬프트 생성 메소드
    // 우리가 전달해줘야하는 데이터

    /* ### example ###
    StringBuilder prompt = new StringBuilder();
    prompt.append("저는 운동을 잘하고 싶습니다. 체력과 체형을 고려하여 맞춤형 운동 루틴을 만들어 주세요.");
    prompt.append(String.format(" 오늘의 운동 루틴에는 다음을 포함해 주세요: 제목은 재치있고 재미있게, 날짜는 %s, 운동 설명과 동영상은 일치하는 운동으로 추천해 주세요.",
                                LocalDateTime.now().toString()));

    // 유저 정보 추가
    prompt.append(String.format(" 키: %d, 몸무게: %d, 성별: %s, 나이: %d, 운동 부위: %s, 운동할 시간: %d분",
                                user.getHeight(), user.getWeight(), user.getGender(), user.getAge(), bodyPart, time));
     */

    // 나짱이가 보내줬던 형식으로 생성하도록

    public String generateRoutine
            (UserEntity userCode, String bodyPart, int time) {
        // 유저코드를 통해 유저에서 유저의 키, 나이, 성별, 몸무게를 가져온다.
        // if 헬스장을 등록한 회원이라면 equipmentPerGymService에서 헬스장별 운동부위별 운동기구 이름 조회
        // else exerciseEquipmentService에서 운동부위별 운동기구 이름 조회
        // generatePrompt 메서드에 파라미터값으로 userCode, equipmentPerBodyPart ,bodyPart, time 전달하고 리턴값을 callOpenAI에 전달
        return null;
    }

    // 3. 프론트에서는 받아온 운동 루틴의 세트수, 반복 횟수를 수정할 수 있고 하고싶지 않은 운동은 제외 하고 저장(DB에 저장하는 개념이 아님)하고, 시작을 누르면 DB에 저장을 위해 백엔드로 요청(JSON형식)
    // 4. 백엔드에서는 수정된 루틴을 저장할 수 있음
}

