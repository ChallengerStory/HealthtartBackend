package com.dev5ops.healthtart.recommended_workout_history.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class RecommendedWorkoutHistoryDTO {

    @JsonProperty("history_code")
    private Long historyCode;

    @JsonProperty("routine_ratings")
    private Double routineRatings;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("routine_code")
    private Long routineCode;



}
