package com.dev5ops.healthtart.record_per_user.aggregate;

import com.google.api.client.util.DateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "RecordPerUser")
@Table(name = "record_per_user")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class RecordPerUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_record_code")
    private Long userRecordCode;

    @Column(name="day_of_exercise")
    private DateTime dayOfExercise;

    @Column(name="exercise_duration")
    private DateTime exerciseDuration;

    @Column(name="ratings")
    private int ratings;

    @Column(name="record_flag")
    private boolean recordFlag;

    @Column(name="created_at")
    private DateTime createdAt;

    @Column(name="updated_at")
    private DateTime updatedAt;

    @Column(name="user_code")
    private String userCode;

    @Column(name="workout_per_routine_code")
    private Long workoutPerRoutineCode;

}
