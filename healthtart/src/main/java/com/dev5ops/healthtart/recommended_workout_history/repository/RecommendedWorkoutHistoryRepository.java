package com.dev5ops.healthtart.recommended_workout_history.repository;

import com.dev5ops.healthtart.recommended_workout_history.domain.dto.RecommendedWorkoutHistoryDTO;
import com.dev5ops.healthtart.recommended_workout_history.domain.entity.RecommendedWorkoutHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendedWorkoutHistoryRepository extends JpaRepository<RecommendedWorkoutHistory, Long> {

    List<RecommendedWorkoutHistoryDTO> findByRoutineCode(Long routineCode);
}

