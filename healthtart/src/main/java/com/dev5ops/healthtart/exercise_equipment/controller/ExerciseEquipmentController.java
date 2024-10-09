package com.dev5ops.healthtart.exercise_equipment.controller;

import com.dev5ops.healthtart.exercise_equipment.aggregate.vo.request.RequestEditEquipmentVO;
import com.dev5ops.healthtart.exercise_equipment.aggregate.vo.request.RequestRegisterEquipmentVO;
import com.dev5ops.healthtart.exercise_equipment.aggregate.vo.response.ResponseEditEquipmentVO;
import com.dev5ops.healthtart.exercise_equipment.aggregate.vo.response.ResponseRegisterEquipmentVO;
import com.dev5ops.healthtart.exercise_equipment.dto.ExerciseEquipmentDTO;
import com.dev5ops.healthtart.exercise_equipment.service.ExerciseEquipmentService;
import com.dev5ops.healthtart.gym.aggregate.vo.request.RequestEditGymVO;
import com.dev5ops.healthtart.gym.aggregate.vo.request.RequestRegisterGymVO;
import com.dev5ops.healthtart.gym.aggregate.vo.response.ResponseEditGymVO;
import com.dev5ops.healthtart.gym.aggregate.vo.response.ResponseFindGymVO;
import com.dev5ops.healthtart.gym.aggregate.vo.response.ResponseRegisterGymVO;
import com.dev5ops.healthtart.gym.dto.GymDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController("exerciseEquipmentController")
@RequestMapping("exercise_equipment")
@Slf4j
public class ExerciseEquipmentController {
    private final ExerciseEquipmentService exerciseEquipmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ExerciseEquipmentController(ExerciseEquipmentService exerciseEquipmentService, ModelMapper modelMapper) {
        this.exerciseEquipmentService = exerciseEquipmentService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "관리자 - 운동기구 등록")
    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterEquipmentVO> registerEquipment(@RequestBody RequestRegisterEquipmentVO request) {
        ExerciseEquipmentDTO equipmentDTO = modelMapper.map(request, ExerciseEquipmentDTO.class);
        ExerciseEquipmentDTO registerEquipment = exerciseEquipmentService.registerEquipment(equipmentDTO);

        ResponseRegisterEquipmentVO response = new ResponseRegisterEquipmentVO(
                registerEquipment.getExerciseEquipmentCode(),
                registerEquipment.getExerciseEquipmentName(),
                registerEquipment.getBodyPart(),
                registerEquipment.getExerciseDescription(),
                registerEquipment.getExerciseImage(),
                registerEquipment.getRecommendedVideo(),
                registerEquipment.getCreatedAt(),
                registerEquipment.getUpdatedAt(),
                registerEquipment.getEquipmentPerGyms()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "관리자 - 운동기구 정보 수정")
    @PatchMapping("/{exerciseEquipmentCode}/edit")
    public ResponseEntity<ResponseEditEquipmentVO> editEquipment(@PathVariable("exerciseEquipmentCode") Long exerciseEquipmentCode, @RequestBody RequestEditEquipmentVO request) {
        ExerciseEquipmentDTO updatedEquipment = exerciseEquipmentService.editEquipment(exerciseEquipmentCode, request);
        ResponseEditEquipmentVO response = new ResponseEditEquipmentVO(
                updatedEquipment.getExerciseEquipmentName(),
                updatedEquipment.getBodyPart(),
                updatedEquipment.getExerciseDescription(),
                updatedEquipment.getExerciseImage(),
                updatedEquipment.getRecommendedVideo(),
                updatedEquipment.getUpdatedAt(),
                updatedEquipment.getEquipmentPerGyms()
        );

        return ResponseEntity.ok(response);
    }
}
