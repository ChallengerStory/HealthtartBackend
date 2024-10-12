package com.dev5ops.healthtart.recommended_workout_history.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestRegisterRecommendedWorkoutHistoryVO {
    private int routineRatings;
    private Long routineCode;
}

