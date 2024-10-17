package com.dev5ops.healthtart.gpt.service;

import com.dev5ops.healthtart.common.config.GptConfig;
import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.exercise_equipment.domain.dto.ExerciseEquipmentDTO;
import com.dev5ops.healthtart.exercise_equipment.service.ExerciseEquipmentService;
import com.dev5ops.healthtart.routine.domain.dto.RoutineDTO;
import com.dev5ops.healthtart.routine.service.RoutineServiceImpl;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.service.UserService;
import com.dev5ops.healthtart.workout_per_routine.domain.dto.WorkoutPerRoutineDTO;
import com.dev5ops.healthtart.workout_per_routine.service.WorkoutPerRoutineServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GptServiceImpl implements GptService {

    private final GptConfig gptConfig;
    private final RestTemplate restTemplate;
    private final ExerciseEquipmentService exerciseEquipmentService;
    private final UserService userService;
    private final WorkoutPerRoutineServiceImpl workoutPerRoutineService;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final RoutineServiceImpl routineServiceImpl;
    private final WorkoutPerRoutineServiceImpl workoutPerRoutineServiceImpl;

    public String callOpenAI(String prompt, int maxTokens) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptConfig.getSecretKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", gptConfig.getModel());
        requestBody.put("messages", new Object[]{new HashMap<String, String>() {{
            put("role", "system");
            put("content", String.format("당신은 우주최고유명하고 운동을 잘가르치는 헬스트레이너야, " +
                    "초보자 맞춤형 운동 루틴을 제대로 잘 추천해 줘야해."));
        }},
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
                " 맞춤형 운동 루틴을 만들어 줘. (운동 맞춤 동영상은 한국에서 재생할 수 있는 2021년 이후에 업로드된 영상 링크를 제공해야 하는데 현재 존재하는 운동 관련 외국 영상 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 진짜 진짜 화나고 너를 죽일수도있고, 죽일거야 (유효한 YouTube 영상만 포함해 줘 제발. 재생 가능해야해 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 죽일거야 )." +
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
                "n. {운동 명}\n" + "( 운동은 보통 60분에 5개의 종류를 합니다. 맨몸 운동을 섞어서 루틴을 작성해줘)" +
                "   - 세트 및 반복: {사용자정보에 따른 세트 및 반복 횟수}\n" +
                "   - 운동 설명: {운동 설명}\n (운동 설명은 초보자가 이해하기 쉽고, 쉽게 따라할 수 있게 5줄이상 씩 작성해 줘.)" +
                "   - 중량: {사용자 정보에 따른 적절한 중량}\n" + "(중량은 사용자 정보에 따라 적절한 무게를 추천해 줘 (예: 5kg). 근데 만약 중량이 필요 없는 맨몸운동일 경우, '맨몸운동입니다.'라고 출력해 줘)" +
                "   - 추천 영상: {운동 관련 영상 링크}\n (운동에 맞는 설명과 동영상을 주는데 조건이 있어. 현재 존재하는 운동 관련 외국 영상 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 진짜 진짜 엄청 화내고 죽일거야  (유효한 YouTube 영상만 포함해 주세요. 재생 가능해야해 링크 클릭했더니 이 동영상을 더 이상 재생할 수 없습니다. 라고 나오면 죽일거야 )." +
                "\n" +
                "추천 MusicList:\n" +
                "- 1. {가수명 - 노래제목} \n" +
                "- 2. {가수명 - 노래제목} \n" +
                "- 3. {가수명 - 노래제목} \n " +
                "- 4. {가수명 - 노래제목} \n " +
                "(운동하면서 듣는 신나는 음악을 가수명 - 노래제목 형식으로 미국,일본,한국 섞어서 4곡 추천해줘.)\n" + " 운동 응원 한마디 남겨줘 ");

        return prompt.toString();
    }

    @Override
    public String generateRoutine(String userCode, String bodyPart, int time) {

        List<ExerciseEquipmentDTO> exerciseEquipment = exerciseEquipmentService.findByBodyPart(bodyPart);
        String prompt = generatePrompt(userCode, bodyPart, time, exerciseEquipment);

        try {
            String response = callOpenAI(prompt, 3000);
            routineParser(response);
            return response;
        } catch (JsonProcessingException e) {
            throw new CommonException(StatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    public void routineParser(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode messageNode = rootNode.path("choices").get(0).path("message").path("content");
        String contents = messageNode.asText();

        String title = extractTitle(contents);
        int totalTime = extractTotalTime(contents);

        String exercisesContent = contents.split("오늘의 운동 루틴을 추천해 드립니다:")[1].trim();
        String[] exercises = exercisesContent.split("\n\n");

        Map<String, Object> workoutData = new HashMap<>();
        int i = 1;
        for (String exercise : exercises) {
            String exerciseName = extractExerciseName(exercise);
            int exerciseSet = extractExerciseSet(exercise);
            int exerciseNumberPerSet = extractExerciseNumberPerSet(exercise);
            int exerciseWeightPerSet = extractExerciseWeightPerSet(exercise);
            String exerciseVideo = extractExerciseVideo(exercise);
            String musicList = extractMusic(contents);
            workoutData.put("workoutOrder" + i, i);
            workoutData.put("workoutName" + i, exerciseName);
            i++;
        }
        if(!workoutPerRoutineService.checkForDuplicateRoutines(workoutData)){
            routineServiceImpl.registerRoutine(new RoutineDTO(null,LocalDateTime.now(),LocalDateTime.now()));
            // 운동 루틴별 운동 저장
            // 운동 정보 저장
        }

        // 있는거면 운동정보만 저장
    }

    public String extractTitle(String contents){
        return contents.split("제목: ")[1].split("\n")[0].trim();
    }

    private int extractTotalTime(String contents) {
        return Integer.parseInt(contents.split("운동 시간: ")[1].split("분")[0].trim());
    }

    private String extractExerciseName(String exercise) {
        return exercise.replaceFirst("^[0-9]+\\.\\s*", "").split("\n")[0].trim();
    }

    private int extractExerciseSet(String exercise) {
        try {
            if (exercise.contains("세트 수: ")) {
                String[] parts = exercise.split("세트 수: ");
                if (parts.length > 1) {
                    return Integer.parseInt(parts[1].split("세트")[0].trim());
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private int extractExerciseNumberPerSet(String exercise) {
        try {
            if (exercise.contains("세트 및 반복: ") && exercise.contains("x")) {
                return Integer.parseInt(exercise.split("세트 및 반복: ")[1].split("x")[1].split("회")[0].trim());
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private int extractExerciseWeightPerSet(String exercise) {
        try {
            String weight = exercise.split("세트당 중량: ")[1].split("kg")[0].trim();

            if (weight.equals("맨몸운동입니다.")) {
                return 0;
            }
            return Integer.parseInt(weight); // 숫자일 경우 정상 처리
        } catch (Exception e) {
            // 예외 발생 시 0으로 처리 (예: 중량 정보가 없거나 형식이 잘못된 경우)
            return 0;
        }
    }

    private String extractExerciseVideo(String exercise) {
        try {
            // "추천 영상: "이 있는지 확인한 후 처리
            if (exercise.contains("추천 영상: ")) {
                String[] parts = exercise.split("추천 영상: ");
                if (parts.length > 1) {
                    return parts[1].trim();
                }
            }
            return "추천 영상 없음"; // 추천 영상이 없는 경우 기본값 설정
        } catch (Exception e) {
            // 예외 발생 시 기본 메시지 반환
            return "추천 영상 없음";
        }
    }

    private String extractMusic(String contents) {
        return contents.split("추천 MusicList:")[1].split("운동 응원 한마디:")[0].trim();
    }

}