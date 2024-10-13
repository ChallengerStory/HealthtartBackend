package com.dev5ops.healthtart.routine.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestInsertRoutineVO {
    private Long routineCode;
    private String title;
    private Integer time;
    private String link;
    private String recommendMusic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
