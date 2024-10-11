package com.dev5ops.healthtart.record_per_user.aggregate.vo.request;

import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestRegistRecordPerUserVO {
    private Long userRecordCode;
    private LocalDate dayOfExercise;
    private LocalTime exerciseDuration;
    private boolean recordFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserEntity userCode;
    private Long workoutPerRoutineCode;
}
