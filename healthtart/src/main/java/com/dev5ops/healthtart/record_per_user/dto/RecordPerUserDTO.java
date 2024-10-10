package com.dev5ops.healthtart.record_per_user.dto;

import com.dev5ops.healthtart.user.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecordPerUserDTO {

    @JsonProperty("user_record_code")
    private Long userRecordCode;

    @JsonProperty("day_of_exercise")
    private LocalDate dayOfExercise;

    @JsonProperty("exercise_duration")
    private LocalTime exerciseDuration;

    @JsonProperty("record_flag")
    private boolean recordFlag;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("user_code")
    private User userCode;

    @JsonProperty("workout_per_routine_code")
    private Long workoutPerRoutineCode;

    @Override
    public String toString() {
        return "RecordPerUser{" +
                "userRecordCode=" + userRecordCode +
                ", dayOfExercise=" + dayOfExercise +
                ", exerciseDuration=" + exerciseDuration +
                ", recordFlag=" + recordFlag +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userCode=" + (userCode != null ? userCode.getUserCode() : null) +
                ", workoutPerRoutineCode=" + workoutPerRoutineCode +
                '}';
    }

}


