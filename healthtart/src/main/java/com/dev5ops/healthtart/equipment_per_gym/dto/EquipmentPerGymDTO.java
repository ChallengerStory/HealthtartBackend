package com.dev5ops.healthtart.equipment_per_gym.dto;

import com.dev5ops.healthtart.exercise_equipment.aggregate.ExerciseEquipment;
import com.dev5ops.healthtart.gym.aggregate.Gym;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EquipmentPerGymDTO {

    @JsonProperty("equipment_per_gym_code")
    private Long equipmentPerGymCode;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("gym")
    private Gym gym;

    @JsonProperty("exercise_equipment")
    private ExerciseEquipment exerciseEquipment;
}
