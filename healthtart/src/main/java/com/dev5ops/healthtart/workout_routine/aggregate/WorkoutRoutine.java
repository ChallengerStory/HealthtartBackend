
package com.dev5ops.healthtart.workout_routine.aggregate;

import com.google.type.DateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "WorkoutRoutine")
@Table(name = "workout_routine")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class WorkoutRoutine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="routine_code")
    private Long routineCode;

    @Column(name = "title")
    private String title;

    @Column(name = "time")
    private DateTime time;

    @Column(name = "link")
    private String link;

    @Column(name = "created_at")
    private DateTime createdAt;

    @Column(name = "updated_at")
    private DateTime updatedAt;
}
