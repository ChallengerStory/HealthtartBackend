package com.dev5ops.healthtart.record_per_user.domain.vo.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseFindPerUserVO {
    private LocalDate dayOfExercise;
    private LocalDateTime exerciseDuration;
}
