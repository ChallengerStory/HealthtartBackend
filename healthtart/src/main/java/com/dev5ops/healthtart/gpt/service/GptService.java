package com.dev5ops.healthtart.gpt.service;

import com.dev5ops.healthtart.common.config.GptConfig;
import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.exercise_equipment.domain.dto.ExerciseEquipmentDTO;
import com.dev5ops.healthtart.exercise_equipment.service.ExerciseEquipmentService;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GptService {

    private final GptConfig gptConfig;
    private final RestTemplate restTemplate;
    private final ExerciseEquipmentService exerciseEquipmentService;
    private final UserService userService;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String callOpenAI(String prompt, int maxTokens) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptConfig.getSecretKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", gptConfig.getModel());
        requestBody.put("messages", new Object[]{ new HashMap<String, String>() {{
                    put("role", "system");
                    put("content", String.format("당신은 우주최고유명하고 운동을 잘가르치는 헬스트레이너야, " +
                            "초보자 맞춤형 운동 루틴을 제대로 잘 추천해 줘야해."));}},
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });

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

    public String generatePrompt(String userCode, String bodyPart, int time, List<ExerciseEquipmentDTO> exerciseEquipmentDTO) {
        UserDTO user = userService.findById(userCode);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formattedDate = LocalDateTime.now().format(formatter);

        StringBuilder prompt = new StringBuilder();
        prompt.append("저는 엄청 운동을 잘하고 싶은 초보자야. 키, 몸무게, 성별, 나이 그리고 원하는 운동부위와 운동할 시간을 제공할테니" +
                " 맞춤형 운동 루틴을 만들어 줘. (운동 맞춤 동영상은 한국에서 재생할 수 있는 2021년 이후에 업로드된 영상 링크를 제공해야 하는데 현재 존재하는 운동 관련 외국 영상 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 진짜 진짜 화나고 너를 죽일수도있고, 죽일거야 (유효한 YouTube 영상만 포함해 줘 제발. 재생 가능해야해 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 죽일거야 )."+
                "같은 사용자가 같은 정보로 운동 루틴을 재요청한다면 이전에 추천해준 운동루틴과 다른 운동루틴을 추천해 줘 " +
                "음악도 매번 다르게 추천해 줘.\n" +
                "오늘의 운동 루틴에는 형식과 조건을 꼭 지켜줘:\n");

        prompt.append(String.format(" 날짜 : %s, 키: %.0fcm, 몸무게: %.0fkg, 성별: %s, 나이: %d세, 운동 부위: %s, 운동할 시간: %d분"
                , formattedDate
                , user.getUserHeight()
                , user.getUserWeight()
                , user.getUserGender()
                , user.getUserAge()
                , bodyPart
                , time));
        String equipmentList = exerciseEquipmentDTO.stream()
                .map(ExerciseEquipmentDTO::getExerciseEquipmentName)
                .collect(Collectors.joining(", "));

        prompt.append("운동 추천을 위해 사용할 수 있는 운동 기구 리스트도 제공할게");
        prompt.append(String.format(" 운동에 필요한 기구: %s", equipmentList));

        prompt.append("아래에 제공된 형식을 사용해 줘 꼭!!!");
        prompt.append("**오늘의 운동 루틴**\n" +
                "제목: {운동 목적에 맞는 제목}\n" + "(제목은 재치있고 재미있게 작성해 줘)" +
                "날짜: {현재 날짜}\n" + "(날짜는 %s로 설정해줘.)" +
                "운동 부위: {사용자가 선택한 운동 부위}\n" +
                "키: {사용자 키}\n" +
                "몸무게: {사용자 몸무게}\n" +
                "성별: {사용자 성별}\n" +
                "나이: {사용자 나이}\n" +
                "운동 시간: {사용자가 입력한 운동 시간}\n" +
                "\n" +
                "오늘의 운동 루틴을 추천해 드립니다:\n" +
                "\n" +
                "n. {운동 명}\n" + "( 운동은 보통 60분에 5개의 종류를 합니다. 맨몸 운동을 섞어서 루틴을 작성해줘)"+
                "   - 세트 및 반복: {사용자정보에 따른 세트 및 반복 횟수}\n" +
                "   - 운동 설명: {운동 설명}\n (운동 설명은 초보자가 이해하기 쉽고, 쉽게 따라할 수 있게 5줄이상 씩 작성해 줘.)" +
                "   - 중량: {사용자 정보에 따른 적절한 중량}\n" + "(중량은 사용자 정보에 따라 적절한 무게를 추천해 줘 (예: 5kg). 근데 만약 중량이 필요 없는 맨몸운동일 경우, '맨몸운동입니다.'라고 출력해 줘)"+
                "   - 추천 영상: {운동 관련 영상 링크}\n (운동에 맞는 설명과 동영상을 주는데 조건이 있어. 현재 존재하는 운동 관련 외국 영상 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 진짜 진짜 엄청 화내고 죽일거야  (유효한 YouTube 영상만 포함해 주세요. 재생 가능해야해 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 죽일거야 )." +
                "\n" +
                "추천 MusicList:\n" +
                "- 1. {가수명 - 노래제목} \n" +
                "- 2. {가수명 - 노래제목} \n" +
                "- 3. {가수명 - 노래제목} \n "+
                "- 4. {가수명 - 노래제목} \n "+
                "(운동하면서 듣는 신나는 음악을 가수명 - 노래제목 형식으로 미국,일본,한국 섞어서 4곡 추천해줘.)\n"+" 운동 응원 한마디 남겨줘 ");

        return prompt.toString();
    }

    public String generateRoutine(String userCode, String bodyPart, int time) {

        List<ExerciseEquipmentDTO> exerciseEquipment = exerciseEquipmentService.findByBodyPart(bodyPart);
        String prompt = generatePrompt(userCode, bodyPart, time, exerciseEquipment);

        try {
            return callOpenAI(prompt, 3000);
        } catch (JsonProcessingException e) {
            throw new CommonException(StatusEnum.INTERNAL_SERVER_ERROR);
        }
    }
}



