package com.dev5ops.healthtart.workout_per_routine.domain.vo.response;

import com.dev5ops.healthtart.exercise_equipment.domain.entity.ExerciseEquipment;
import com.dev5ops.healthtart.routine.domain.entity.Routine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseFindWorkoutPerRoutineVO {
    private Long workoutPerRoutineCode;
    private int workoutOrder;
    private String workoutName;
    private String link;
    private int weightSet;
    private int numberPerSet;
    private int weightPerSet;
    private int workoutTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Routine routineCode;
    private ExerciseEquipment exerciseEquipmentCode;


}
