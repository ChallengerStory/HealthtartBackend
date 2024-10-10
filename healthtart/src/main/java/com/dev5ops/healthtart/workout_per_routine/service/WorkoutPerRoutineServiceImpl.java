package com.dev5ops.healthtart.workout_per_routine.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.workout_per_routine.domain.dto.WorkoutPerRoutineDTO;
import com.dev5ops.healthtart.workout_per_routine.domain.entity.WorkoutPerRoutine;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.vo.*;
import com.dev5ops.healthtart.workout_per_routine.repository.WorkoutPerRoutineRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutPerRoutineServiceImpl implements WorkoutPerRoutineService {

    private final WorkoutPerRoutineRepository workoutPerRoutineRepository;
    private final ModelMapper modelMapper;

    // 운동 루틴별 운동 전체 조회
    @Override
    public List<ResponseFindWorkoutPerRoutineVO> getWorkoutPerRoutines() {
        List<WorkoutPerRoutine> routinesList = workoutPerRoutineRepository.findAll();
        if (routinesList.isEmpty()) throw new CommonException(StatusEnum.ROUTINE_NOT_FOUND);
        return routinesList.stream()
                .map(routine -> modelMapper.map(routine, ResponseFindWorkoutPerRoutineVO.class))
                .collect(Collectors.toList());
    }

    // 운동 루틴별 운동 단일 조회
    @Override
    public ResponseFindWorkoutPerRoutineVO findWorkoutPerRoutineByCode(Long workoutPerRoutineCode) {
        WorkoutPerRoutine routine = workoutPerRoutineRepository.findById(workoutPerRoutineCode)
                .orElseThrow(() -> new CommonException(StatusEnum.ROUTINE_NOT_FOUND));
        return modelMapper.map(routine, ResponseFindWorkoutPerRoutineVO.class);
    }



}
