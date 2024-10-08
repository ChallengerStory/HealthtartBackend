package com.dev5ops.healthtart.recommended_workout_history.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.type.DateTime;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class RecommendedWorkoutHistoryDTO {

    @JsonProperty("history_code")
    private Long historyCode;

    @JsonProperty("routine_ratings")
    private int routineRatings;

    @JsonProperty("recommended_time")
    private int recommendedTime;

    @JsonProperty("created_at")
    private DateTime createdAt;

    @JsonProperty("updated_at")
    private DateTime updatedAt;

    @JsonProperty("routine_code")
    private Long routineCode;



}
