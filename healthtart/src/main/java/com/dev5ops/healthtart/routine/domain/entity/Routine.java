
package com.dev5ops.healthtart.routine.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.type.DateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity(name = "WorkoutRoutine")
@Table(name = "workout_routine")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
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
}
