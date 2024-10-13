package com.dev5ops.healthtart.workout_per_routine.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EditWorkoutPerRoutineVO {
    private int workoutOrder;
    private int weightSet;
    private int numberPerSet;
    private int weightPerSet;
    private int workoutTime;

}
