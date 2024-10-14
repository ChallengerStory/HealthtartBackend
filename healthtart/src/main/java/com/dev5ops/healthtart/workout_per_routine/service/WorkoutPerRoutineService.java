package com.dev5ops.healthtart.workout_per_routine.service;

import com.dev5ops.healthtart.workout_per_routine.domain.dto.WorkoutPerRoutineDTO;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.*;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.response.ResponseDeleteWorkoutPerRoutineVO;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.response.ResponseFindWorkoutPerRoutineVO;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.response.ResponseInsertWorkoutPerRoutineVO;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.response.ResponseModifyWorkoutPerRoutineVO;
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
