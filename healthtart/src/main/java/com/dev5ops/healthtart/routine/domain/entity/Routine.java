
package com.dev5ops.healthtart.routine.domain.entity;

import com.dev5ops.healthtart.routine.domain.vo.EditRoutineVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Entity(name = "WorkoutRoutine")
@Table(name = "workout_routine")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="routine_code")
    private Long routineCode;

    @Column(name = "title")
    private String title;

    @Column(name = "time")
    private Integer time;

    @Column(name = "link")
    private String link;

    @JsonProperty("recommend_music")
    private String recommendMusic;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void toUpdate(@Validated EditRoutineVO editRoutineVO) {
        this.title = editRoutineVO.getTitle();
        this.time = editRoutineVO.getTime();
    }

    }
