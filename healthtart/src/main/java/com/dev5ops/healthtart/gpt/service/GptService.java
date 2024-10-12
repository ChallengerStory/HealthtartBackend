package com.dev5ops.healthtart.gpt.service;

import com.dev5ops.healthtart.common.config.GptConfig;
import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.exercise_equipment.domain.dto.ExerciseEquipmentDTO;
import com.dev5ops.healthtart.exercise_equipment.service.ExerciseEquipmentService;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.service.UserServiceImpl;
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

    private final UserServiceImpl userServiceImpl;
    private final ExerciseEquipmentService exerciseEquipmentService;
    private final GptConfig gptConfig;
    private final RestTemplate restTemplate;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String callOpenAI(String prompt, int maxTokens) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // JSON형식으로 데이터를 주고 받을 것임을 알림
        headers.setBearerAuth(gptConfig.getSecretKey());    // 헤더에 Open AI 키 추가
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formattedDate = LocalDateTime.now().format(formatter);
        Map<String, Object> requestBody = new HashMap<>();  // requestBody에 API에 요청할 내용을 담은 JSON형식의 데이터 준비
        requestBody.put("model", gptConfig.getModel());     // 어떤 모델을 사용할건지(gptConfig에 적어둔)
        requestBody.put("messages", new Object[]{           // API로 보내는 사용자 메시지를 정의한 부분
                new HashMap<String, String>() {{
                    put("role", "system");
                    put("content", String.format("당신은 헬스트레이너입니다. 초보자 맞춤형 운동 루틴을 추천해 줘야 합니다. " +
                                    "같은 사용자가 같은 정보로 운동 루틴을 재요청한다면 이전에 추천해준 운동루틴과 다른 운동루틴을 추천해 주세요. " +
                                    "음악도 매번 다르게 추천해 주세요.\n" +
                                    "오늘의 운동 루틴에는 다음을 포함해 주세요:\n" +
                                    "1. 운동은 보통 60분에 5개의 종류를 합니다. 맨몸 운동을 섞어서 루틴을 작성해 주세요.\n" +
                                    "2. 제목은 재치있고 재미있게 작성해 주세요.\n" +
                                    "3-1. 날짜는 %s로 설정하고, 운동에 맞는 설명과 동영상을 추천해 주세요.\n" +
                                    "3-2. 운동 설명은 초보자가 이해하기 쉽게 설명해 주세요.\n" +
                                    "3-3. 동영상은 한국에서 재생할 수 있는 2021년 이후에 업로드된 영상 링크를 제공해 주세요. (현재 존재하는 운동 관련 외국 영상 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 진짜 화내고 죽일거야  (유효한 YouTube 영상만 포함해 주세요. 재생 가능해야해 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 죽일거야 )." +
                                    "4-1. 중량은 사용자 정보에 따라 적절한 무게를 추천해 주세요 (예: 5kg).\n" +
                                    "4-2. 만약 중량이 필요 없는 맨몸운동일 경우, '맨몸운동입니다.'라고 출력해 주세요.\n" +
                                    "5. 마지막에는 운동과 어울리는 신나는 음악을 가수명 - 노래제목 형식으로 3곡 추천해 주세요.",
                            formattedDate));
                }},

                new HashMap<String, String>() {{
                    put("role", "user");                    // role은 유저, (근데 이 role부분에 뭘 적느냐에 따라 어떻게 달라지는지를 모르곘네)
                    put("content", prompt);                 // contents는 작성한 prompt 전달
                }}
        });

        requestBody.put("temperature", 0.7);                // GPT 응답 다양성을 제어하는 매개변수
        requestBody.put("max_tokens", maxTokens);           // 응답에 포함될 최대 토큰 수

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // 2. prompt는 넘어온 userDTO, bodyPart, time, exerciseEquipmentDTO를 통해 프롬프트를 생성하고 return
    public String generatePrompt(UserDTO userDTO
            , String bodyPart
            , int time
            , List<ExerciseEquipmentDTO> exerciseEquipmentDTO) {


        StringBuilder prompt = new StringBuilder();
        prompt.append("저는 운동을 잘하고 싶은 초보자입니다. 키, 몸무게, 성별, 나이 그리고 원하는 운동부위와 운동할 시간을 제공할테니" +
                " 맞춤형 운동 루틴을 만들어 주세요. (현재 존재하는 운동 관련 외국 영상 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 진짜 화내고 죽일거야  (유효한 YouTube 영상만 포함해 주세요. 재생 가능해야해 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 죽일거야 ).");

        prompt.append(String.format(" 키: %f, 몸무게: %f, 성별: %s, 나이: %d, 운동 부위: %s, 운동할 시간: %d분"
                , userDTO.getUserHeight()
                , userDTO.getUserWeight()
                , userDTO.getUserGender()
                , userDTO.getUserAge()
                , bodyPart
                , time));

        String equipmentList = exerciseEquipmentDTO.stream()
                .map(ExerciseEquipmentDTO::getExerciseEquipmentName)
                .collect(Collectors.joining(", "));

        prompt.append("운동 추천을 위해 사용할 수 있는 운동 기구 리스트도 제공하겠습니다.");
        prompt.append(String.format(" 운동에 필요한 기구: %s", equipmentList));
        System.out.println(equipmentList);

        prompt.append("아래에 제공된 형식을 사용해 주세요.");
        prompt.append("**운동 루틴**\n" +
                "제목: {운동 목적에 맞는 제목}\n" +
                "날짜: {현재 날짜}\n" +
                "운동 부위: {사용자가 선택한 운동 부위}\n" +
                "키: {사용자 키}\n" +
                "몸무게: {사용자 몸무게}\n" +
                "성별: {사용자 성별}\n" +
                "나이: {사용자 나이}\n" +
                "운동 시간: {사용자가 입력한 운동 시간}\n" +
                "\n" +
                "오늘의 운동 루틴을 추천해 드립니다:\n" +
                "\n" +
                "n. {운동 명}\n" +
                "   - 세트 및 반복: {사용자정보에 따른 세트 및 반복 횟수}\n" +
                "   - 운동 설명: {운동 설명}\n" +
                "   - 중량: {사용자 정보에 따른 적절한 중량}\n" +
                "   - 추천 영상: {운동 관련 영상 링크}\n (현재 존재하는 운동 관련 외국 영상 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 진짜 화내고 죽일거야  (유효한 YouTube 영상만 포함해 주세요. 재생 가능해야해 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 죽일거야 )." +
                "\n" +
                "추천 MusicList:\n" +
                "- 1. {가수명 - 노래제목} \n" +
                "- 2. {가수명 - 노래제목} \n" +
                "- 3. {가수명 - 노래제목} \n");

        return prompt.toString();
    }

    // 1. 로그인한 회원이 운동부위와 시간을 선택하면 루틴생성을 위해 userCode, bodyPart를 사용해 유저정보와 운동기구정보를 prompt로 보냄
    // 3. 리턴 값은 String prompt에 저장되어 callOpenAPI에 넘겨지고 루틴 생성됨
    public String generateRoutine(String userCode, String bodyPart, int time) {
        UserDTO userDTO = userServiceImpl.findById(userCode);
        List<ExerciseEquipmentDTO> exerciseEquipmentDTO = exerciseEquipmentService.findByBodyPart(bodyPart);

        String prompt = generatePrompt(userDTO, bodyPart, time, exerciseEquipmentDTO);

        try {
            return callOpenAI(prompt, 3000);
        } catch (JsonProcessingException e) {
            throw new CommonException(StatusEnum.INTERNAL_SERVER_ERROR);
        }
    }
}

// 3. 프론트에서는 받아온 운동 루틴의 세트수, 반복 횟수를 수정할 수 있고 하고싶지 않은 운동은 제외 하고 저장(DB에 저장하는 개념이 아님)하고, 시작을 누르면 DB에 저장을 위해 백엔드로 요청(JSON형식)
// 4. 백엔드에서는 수정된 루틴을 저장할 수 있음


