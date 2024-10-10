package com.dev5ops.healthtart.recommended_workout_history.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity(name = "RecommendedWorkoutHistory")
@Table(name = "recommended_workout_history")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class RecommendedWorkoutHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="history_code")
    private Long historyCode;

    @Column(name="routine_ratings")
    private int routineRatings;

    @Column(name="recommended_time")
    private int recommendedTime;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="routine_code")
    private Long routineCode;

}
