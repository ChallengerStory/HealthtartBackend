package com.dev5ops.healthtart.recommended_workout_history.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseFindByRatingOrderVO {
    private Long workoutInfoCode;
    private Double routineRatings;

}
