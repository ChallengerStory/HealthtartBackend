package com.dev5ops.healthtart.workout_per_routine.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.workout_per_routine.domain.dto.WorkoutPerRoutineDTO;
import com.dev5ops.healthtart.workout_per_routine.domain.entity.WorkoutPerRoutine;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.*;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.response.ResponseDeleteWorkoutPerRoutineVO;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.response.ResponseFindWorkoutPerRoutineVO;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.response.ResponseInsertWorkoutPerRoutineVO;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.response.ResponseModifyWorkoutPerRoutineVO;
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

    // 루틴별 운동 등록
    @Override
    @Transactional
    public ResponseInsertWorkoutPerRoutineVO registerWorkoutPerRoutine(WorkoutPerRoutineDTO workoutPerRoutineDTO) {
        validateWorkoutPerRoutineDTO(workoutPerRoutineDTO);

        WorkoutPerRoutine routine = WorkoutPerRoutine.builder()
                .workoutPerRoutineCode(workoutPerRoutineDTO.getWorkoutPerRoutineCode())
                .workoutOrder(workoutPerRoutineDTO.getWorkoutOrder())
                .workoutName(workoutPerRoutineDTO.getWorkoutName())
                .weightSet(workoutPerRoutineDTO.getWeightSet())
                .numberPerSet(workoutPerRoutineDTO.getNumberPerSet())
                .weightPerSet(workoutPerRoutineDTO.getWeightPerSet())
                .workoutTime(workoutPerRoutineDTO.getWorkoutTime())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .exerciseEquipmentCode(workoutPerRoutineDTO.getExerciseEquipmentCode())
                .recordCode(workoutPerRoutineDTO.getRecordCode())
                .build();

        workoutPerRoutineRepository.save(routine);
        return modelMapper.map(routine, ResponseInsertWorkoutPerRoutineVO.class);
    }

    // 루틴별 운동 수정
    @Override
    @Transactional
    public ResponseModifyWorkoutPerRoutineVO modifyWorkoutPerRoutine(Long workoutPerRoutineCode, EditWorkoutPerRoutineVO modifyRoutine) {
        WorkoutPerRoutine routine = workoutPerRoutineRepository.findById(workoutPerRoutineCode)
                .orElseThrow(() -> new CommonException(StatusEnum.ROUTINE_NOT_FOUND));
        routine.toUpdate(modifyRoutine);
        workoutPerRoutineRepository.save(routine);
        return modelMapper.map(routine, ResponseModifyWorkoutPerRoutineVO.class);
    }

    // 루틴별 운동 삭제
    @Override
    @Transactional
    public ResponseDeleteWorkoutPerRoutineVO deleteWorkoutPerRoutine(Long workoutPerRoutineCode) {
        WorkoutPerRoutine routine = workoutPerRoutineRepository.findById(workoutPerRoutineCode)
                .orElseThrow(() -> new CommonException(StatusEnum.ROUTINE_NOT_FOUND));
        workoutPerRoutineRepository.delete(routine);
        return new ResponseDeleteWorkoutPerRoutineVO();
    }

    // DTO 검증
    private void validateWorkoutPerRoutineDTO(WorkoutPerRoutineDTO workoutPerRoutineDTO) {
        if (workoutPerRoutineDTO.getWorkoutPerRoutineCode() == null ||
                workoutPerRoutineDTO.getWorkoutOrder() <= 0 ||
                workoutPerRoutineDTO.getWorkoutTime() <= 0) {
            throw new CommonException(StatusEnum.INVALID_PARAMETER_FORMAT);
        }
    }

}
