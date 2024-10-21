package com.dev5ops.healthtart.record_per_user.domain.vo.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseRegisterRecordPerUserVO {
    private Long userRecordCode;
    private LocalDate dayOfExercise;
    private Integer exerciseDuration;
    private boolean recordFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userCode;
    private Long RoutineCode;

}
