package com.dev5ops.healthtart.workout_per_routine.service;

import com.dev5ops.healthtart.workout_per_routine.domain.dto.WorkoutPerRoutineDTO;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.vo.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WorkoutPerRoutineService {
    List<ResponseFindWorkoutPerRoutineVO> getWorkoutPerRoutines();

    ResponseFindWorkoutPerRoutineVO findWorkoutPerRoutineByCode(Long workoutPerRoutineCode);

    @Transactional
    ResponseInsertWorkoutPerRoutineVO registerWorkoutPerRoutine(WorkoutPerRoutineDTO workoutPerRoutineDTO);

    @Transactional
    ResponseModifyWorkoutPerRoutineVO modifyWorkoutPerRoutine (Long workoutPerRoutineCode, EditWorkoutPerRoutineVO modifyRoutine);

    @Transactional
    ResponseDeleteWorkoutPerRoutineVO deleteWorkoutPerRoutine(Long workoutPerRoutineCode);
}
