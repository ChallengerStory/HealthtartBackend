package com.dev5ops.healthtart.equipment_per_gym.aggregate;

import com.dev5ops.healthtart.exercise_equipment.aggregate.ExerciseEquipment;
import com.dev5ops.healthtart.gym.aggregate.Gym;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "equipment_per_gym")
@Table(name = "equipment_per_gym")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EquipmentPerGym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_per_gym_code", nullable = false, unique = true)
    private Long equipmentPerGymCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "gym_code", nullable = false)
    private Gym gym;

    @ManyToOne
    @JoinColumn(name = "exercise_equipment_code", nullable = false)
    private ExerciseEquipment exerciseEquipment;
}
