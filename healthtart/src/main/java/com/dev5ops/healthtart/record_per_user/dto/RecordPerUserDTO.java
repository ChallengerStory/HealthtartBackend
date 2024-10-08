package com.dev5ops.healthtart.record_per_user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.DateTime;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class RecordPerUserDTO {

    @JsonProperty("user_record_code")
    private Long userRecordCode;

    @JsonProperty("day_of_exercise")
    private DateTime dayOfExercise;

    @JsonProperty("exercise_duration")
    private DateTime exerciseDuration;

    @JsonProperty("ratings")
    private int ratings;

    @JsonProperty("record_flag")
    private boolean recordFlag;

    @JsonProperty("created_at")
    private DateTime createdAt;

    @JsonProperty("updated_at")
    private DateTime updatedAt;

    @JsonProperty("user_code")
    private String userCode;

    @JsonProperty("workout_per_routine_code")
    private Long workoutPerRoutineCode;

}
