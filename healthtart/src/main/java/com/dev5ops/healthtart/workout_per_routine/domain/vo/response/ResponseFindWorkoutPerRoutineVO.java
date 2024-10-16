package com.dev5ops.healthtart.workout_per_routine.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseFindWorkoutPerRoutineVO {
    private Long workoutPerRoutineCode;
    private int workoutOrder;
    private String workoutName;
    private int weightSet;
    private int numberPerSet;
    private int weightPerSet;
    private int workoutTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long recordCode;
    private Long exerciseEquipmentCode;


}
