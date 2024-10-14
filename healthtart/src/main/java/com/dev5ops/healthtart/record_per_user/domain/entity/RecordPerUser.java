package com.dev5ops.healthtart.record_per_user.domain.entity;

import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity(name = "RecordPerUser")
@Table(name = "record_per_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecordPerUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_record_code", nullable = false, unique = true)
    private Long userRecordCode;

    @Column(name="day_of_exercise", nullable = false)
    private LocalDate dayOfExercise;

    @Column(name="exercise_duration", nullable = false)
    private LocalDateTime exerciseDuration;

    @Column(name="record_flag", nullable = false)
    private boolean recordFlag;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_code", nullable = false)
    private UserEntity user;

    @Column(name="workout_per_routine_code", nullable = false)
    private Long workoutPerRoutineCode;

}
