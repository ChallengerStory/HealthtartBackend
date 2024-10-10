package com.dev5ops.healthtart.workout_per_routine.controller;

import com.dev5ops.healthtart.workout_per_routine.domain.dto.WorkoutPerRoutineDTO;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.vo.*;
import com.dev5ops.healthtart.workout_per_routine.service.WorkoutPerRoutineServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout-per-routine")
@RequiredArgsConstructor
public class WorkoutPerRoutineController {

    private final WorkoutPerRoutineServiceImpl workoutPerRoutineService;
    private final ModelMapper modelMapper;


    @Operation(summary = "운동 루틴별 운동 전체 조회")
    @GetMapping
    public ResponseEntity<List<ResponseFindWorkoutPerRoutineVO>> getAllWorkoutPerRoutines() {
        List<ResponseFindWorkoutPerRoutineVO> routines = workoutPerRoutineService.getWorkoutPerRoutines();
        return new ResponseEntity<>(routines, HttpStatus.OK);
    }

    @Operation(summary = "운동 루틴별 운동 단일 조회")
    @GetMapping("/{workoutPerRoutineCode}")
    public ResponseEntity<ResponseFindWorkoutPerRoutineVO> getWorkoutPerRoutineByCode(@PathVariable Long workoutPerRoutineCode) {
        ResponseFindWorkoutPerRoutineVO routine = workoutPerRoutineService.findWorkoutPerRoutineByCode(workoutPerRoutineCode);
        return new ResponseEntity<>(routine, HttpStatus.OK);
    }

    @Operation(summary = "운동 루틴별 운동 등록")
    @PostMapping
    public ResponseEntity<ResponseInsertWorkoutPerRoutineVO> registerWorkoutPerRoutine(@RequestBody RequestInsertWorkoutPerRoutineVO requestInsertWorkoutPerRoutineVO) {
        WorkoutPerRoutineDTO workoutPerRoutineDTO = modelMapper.map(requestInsertWorkoutPerRoutineVO, WorkoutPerRoutineDTO.class);
        ResponseInsertWorkoutPerRoutineVO response = workoutPerRoutineService.registerWorkoutPerRoutine(workoutPerRoutineDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "운동 루틴별 운동 수정")
    @PutMapping("/{workoutPerRoutineCode}")
    public ResponseEntity<ResponseModifyWorkoutPerRoutineVO> modifyWorkoutPerRoutine(
            @PathVariable("workoutPerRoutineCode") Long workoutPerRoutineCode,
            @RequestBody RequestInsertWorkoutPerRoutineVO requestModify) {
        EditWorkoutPerRoutineVO editWorkoutPerRoutineVO = modelMapper.map(requestModify, EditWorkoutPerRoutineVO.class);
        ResponseModifyWorkoutPerRoutineVO response = workoutPerRoutineService.modifyWorkoutPerRoutine(workoutPerRoutineCode, editWorkoutPerRoutineVO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
