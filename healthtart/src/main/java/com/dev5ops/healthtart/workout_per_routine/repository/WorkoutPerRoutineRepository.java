package com.dev5ops.healthtart.workout_per_routine.repository;

import com.dev5ops.healthtart.workout_per_routine.domain.entity.WorkoutPerRoutine;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface WorkoutPerRoutineRepository extends JpaRepository<WorkoutPerRoutine, Long> {
}
