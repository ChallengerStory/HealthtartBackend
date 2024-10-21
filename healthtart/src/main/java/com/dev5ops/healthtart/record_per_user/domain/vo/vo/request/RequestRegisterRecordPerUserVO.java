package com.dev5ops.healthtart.record_per_user.domain.vo.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestRegisterRecordPerUserVO {
    private Long userRecordCode;
    private LocalDate dayOfExercise;
    private Integer exerciseDuration;
    private boolean recordFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userCode;
    private Long RoutineCode;
}
