package com.dev5ops.healthtart.gpt.service;

import com.dev5ops.healthtart.exercise_equipment.domain.dto.ExerciseEquipmentDTO;
import com.dev5ops.healthtart.exercise_equipment.service.ExerciseEquipmentService;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GptServiceTests {

    @Autowired
    private GptService gptService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExerciseEquipmentService exerciseEquipmentService;

    private String userCode;
    private String bodyPart;
    private int workoutTime;

    @BeforeEach
    public void setUp() {
        userCode = "20241007-05bfb06b-8eda-4857-8681-40d1eccb829d";
        bodyPart = "하체";
        workoutTime = 90;
    }

    @Test
    @DisplayName("유저 정보와 헬스기구 정보를 통한 GPT 루틴 생성 테스트")
    public void testGenerateRoutine() throws JsonProcessingException {
        UserDTO userDTO = userService.findById(userCode);
        assertNotNull(userDTO, "유저 정보를 찾을 수 없습니다.");

        List<ExerciseEquipmentDTO> equipmentList = exerciseEquipmentService.findByBodyPart(bodyPart);
        System.out.println("equipmentList = " + equipmentList);
        assertNotNull(equipmentList, "헬스 기구 정보가 조회되지 않았습니다.");
        assertFalse(equipmentList.isEmpty(), "해당 운동 부위에 대한 헬스 기구가 존재하지 않습니다.");

        String response = gptService.generateRoutine(userCode, bodyPart, workoutTime);
        assertNotNull(response, "GPT 응답이 null입니다.");
        assertTrue(response.contains("하체"), "응답에 운동 부위 정보가 포함되지 않았습니다.");
        assertTrue(response.contains("레그 프레스"), "응답에 레그 프레스 정보가 포함되지 않았습니다.");
    }
}
