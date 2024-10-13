package com.dev5ops.healthtart.gpt.controller;

import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.gpt.service.GptService;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.service.UserService;
import com.dev5ops.healthtart.exercise_equipment.service.ExerciseEquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;
    private final UserService userService;
    private final ExerciseEquipmentService exerciseEquipmentService;

    @PostMapping("/generate-routine")
    @Operation(summary = "GPT 운동 루틴 생성")
    public ResponseEntity<String> generateRoutine(@RequestParam String userCode, @RequestParam String bodyPart, @RequestParam int time) {
        try {
            UserDTO userDTO = userService.findById(userCode);
            if (userDTO == null) {
                return ResponseEntity.badRequest().body(StatusEnum.USER_NOT_FOUND.getMessage());
            }

            var equipmentList = exerciseEquipmentService.findByBodyPart(bodyPart);
            if (equipmentList.isEmpty()) {
                return ResponseEntity.badRequest().body(StatusEnum.EQUIPMENT_NOT_FOUND.getMessage());
            }

            String routine = gptService.generateRoutine(userCode, bodyPart, time);
            return ResponseEntity.ok(routine);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(StatusEnum.ROUTINES_CREATED_ERROR.getMessage());
        }
    }
}
