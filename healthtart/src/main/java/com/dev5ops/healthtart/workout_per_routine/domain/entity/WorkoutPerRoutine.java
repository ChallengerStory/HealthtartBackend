package com.dev5ops.healthtart.workout_per_routine.domain.entity;

import com.dev5ops.healthtart.workout_per_routine.domain.vo.EditWorkoutPerRoutineVO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Entity(name = "WorkoutPerRoutine")
@Table(name = "workout_per_routine")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class WorkoutPerRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="workout_per_routine_code")
    private Long workoutPerRoutineCode;

    @Column(name = "workout_order")
    private int workoutOrder;

    @Column(name = "weight_set")
    private int weightSet;

    @Column(name = "number_per_set")
    private int numberPerSet;

    @Column(name = "weight_per_set")
    private int weightPerSet;

    @Column(name = "workout_time")
    private int workoutTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "record_code")
    private Long recordCode;

    @Column(name = "exercise_equipment_code")
    private Long exerciseEquipmentCode;

    public void toUpdate(@Validated EditWorkoutPerRoutineVO editRoutineVO) {
        this.workoutOrder = editRoutineVO.getWorkoutOrder();
        this.weightSet = editRoutineVO.getWeightSet();
        this.numberPerSet = editRoutineVO.getNumberPerSet();
        this.weightPerSet = editRoutineVO.getWeightPerSet();
        this.workoutTime = editRoutineVO.getWorkoutTime();
    }


}
