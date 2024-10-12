package com.dev5ops.healthtart.gpt.service;

import com.dev5ops.healthtart.common.config.GptConfig;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GptService {

    @Autowired
    private GptConfig gptConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String callOpenAI(String prompt, int maxTokens) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptConfig.getSecretKey());

        // system 메시지와 user 메시지를 각각 추가
        Map<String, Object> systemMessage = Map.of("role", "system", "content", "넌 헬스트레이너야");
        Map<String, Object> userMessage = Map.of("role", "user", "content", prompt);

        // requestBody에 system과 user 메시지를 포함한 리스트로 전달
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", gptConfig.getModel());
        requestBody.put("messages", List.of(systemMessage, userMessage));
        requestBody.put("temperature", 0.3);
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
    public String generateRoutine(String userCode, String bodyPart, int time) {
        // userCode로 유저 정보 조회
        UserDTO user = userService.findById(userCode);

        // 유저 정보를 통해 유저의 키, 몸무게, 성별, 나이 조회
        double height = user.getUserHeight();
        double weight = user.getUserWeight();
        String gender = user.getUserGender();
        int age = user.getUserAge();

        // 프롬프트 생성
        String prompt = "저는 운동을 잘하고싶습니다. 체력과 체형을 고려하여 제 체형과 운동 목표에 맞는 맞춤형 운동 루틴을 만들어 줘 그리고, (현재 존재하는 외국 운동 관련 영상 추천해줄때 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 진짜 진짜 찐짜 화내고 죽일거고, 죽이고싶을거같아 잘 추천해줘 재생되게  (유효한 YouTube 영상만 포함해 주세요. 재생 가능해야해 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 죽일거야 ). 제목은 재치있고 재밌게 가장 큰 텍스트로 해줘, 날짜는 반드시 현재 날짜로 설정해줘. 그리고 다음 정보를 포함해 주세요: 키: {사용자의 키 cm}, 몸무게: {사용자의 몸무게 kg}, 성별: {사용자의 성별}, 운동 부위: {사용자가 집중하고 싶은 부위 (예: 상체, 하체, 전신 등)}, 운동할 시간: {사용자가 운동할 수 있는 시간 (예: 1시간)}. **오늘의 운동 루틴**에는 다음을 포함해 주세요: (1시간에 보통 5~7개 종목을 한다고 합니다.) 운동 순서, 운동 시간, 세트 및 반복 횟수, 권장 중량, 자세하고 이해하기 쉬우며 긴 운동 순서 설명 (운동을 처음 접하는 사람도 쉽게 따라할 수 있게 5줄이상 씩 작성해 줘),현재 존재하는 외국 운동 관련 영상 추천해줄때 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 진짜 진짜 찐짜 화내고 죽일거고, 죽이고싶을거같아(유효한 YouTube 영상만 포함해 주세요. 예: https://www.youtube.com/**). 추가로 추천 음악 5개도 포함해 주세요 (운동할 때 들을만한 신나는 노래를 포함해 주세요. 1. 제목-가수 형식 지켜서 일본, 미국, 한국 음악 각각 한 곡 이상 부탁드립니다). \"운동 루틴을 만들어 주세요. 아래 정보를 포함해야 합니다:\\n\\n- 제목: 다리 운동 루틴\\n- ## 날짜: {{현재 날짜를 표시하는 코드 또는 함수로 변경}}\\n- 키: 170cm\\n- 몸무게: 65kg\\n- 성별: 남성\\n- 운동 부위: 다리\\n- 운동할 시간: 30분\\n\\n오늘의 운동 루틴:\\n1. 스쿼트\\n   - 운동 시간: 10분\\n   - 세트 및 반복 횟수: 3세트 12회\\n   - 중량: 체중고려해서 무게 추천해줘 \\n   - 운동 설명: 어깨 너비로 발을 벌리고 무릎이 발끝을 넘지 않도록 하며 깊게 앉아서 일어납니다. 허벅지와 종아리를 고르게 발달시키는 운동입니다. 이거보다 훨씬 길고 자세하게 부탁해\\n   - 현재 존재하는 외국 운동 관련 영상 추천해줄때 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 진짜 진짜 찐짜 화내고 죽일거고, 죽이고싶을거같아운동 영상: https://www.youtube.com/watch?v=U3Hf7Jf5zj0\\n\\n2. 레그 프레스\\n   - 운동 시간: 10분\\n   - 세트 및 반복 횟수: 3세트 15회\\n   - 중량: 60kg\\n   - 운동 설명: 머신을 이용하여 다리를 밀어 올리는 운동으로 대퇴사두근과 종아리를 강화시키는데 효과적입니다. 이거보다 훨씬 길고 자세하게 부탁해\\n   - (링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 죽일거야 )운동 영상: https://www.youtube.com/watch?v=4A3gJ2FJLd0\\n\\n3. 레그 컬\\n   - 운동 시간: 10분\\n   - 세트 및 반복 횟수: 3세트 12회\\n   - 중량: 15kg\\n   - 운동 설명: 무릎을 굽히고 다리 뒤쪽을 골고루 강화시키는 운동입니다. 이거보다 훨씬 길고 자세하게 부탁해\\n   - 운동 영상: https://www.youtube.com/watch?v=Qd6jR4z2wH8\\n\\n추천 Music:\\n1. Eye of the Tiger - Survivor\\n2. Stronger - Kanye West\\n3. Can't Stop the Feeling! - Justin Timberlake\\n4. Uptown Funk - Mark Ronson ft. Bruno Mars\\n5. Shape of You - Ed Sheeran 그리고 마지막에 (운동응원 멘트) 굵은글씨로 추가해줘"
                + generatePrompt(height, weight, gender, age, bodyPart, time);

        // GPT API 호출
        try {
            return callOpenAI(prompt, 3000);
        } catch (JsonProcessingException e) {
            return "Error generating routine: " + e.getMessage();
        }
    }


    // 프롬프트 생성 메서드
    private String generatePrompt(double height, double weight, String gender, int age, String bodyPart, int time) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formattedDate = LocalDateTime.now().format(formatter);

        return String.format("""
                        날짜 : %s
                        키: %.2f cm
                        몸무게: %.2f kg
                        성별: %s
                        나이: %d 세
                        운동 부위: %s
                        운동할 시간: %d분
                        """,
                formattedDate, height, weight, gender, age, bodyPart, time);
    }

}

