package com.dev5ops.healthtart.workoutinfo.repository;

import com.dev5ops.healthtart.routine.domain.entity.Routine;
import com.dev5ops.healthtart.workoutinfo.domain.entity.WorkoutInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkoutInfoRepository extends JpaRepository <WorkoutInfo, Long> {
    Optional<WorkoutInfo> findByRoutineCode(Routine routineCode);
}
