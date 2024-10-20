package com.dev5ops.healthtart.record_per_user.domain.vo.vo.response;

import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseRegisterRecordPerUserVO {
    private Long userRecordCode;
    private LocalDate dayOfExercise;
    private LocalDateTime exerciseDuration;
    private boolean recordFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userCode;
    private Long routineCode;

}
