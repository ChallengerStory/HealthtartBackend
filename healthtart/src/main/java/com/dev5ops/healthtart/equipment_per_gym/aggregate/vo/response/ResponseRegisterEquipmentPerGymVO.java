package com.dev5ops.healthtart.equipment_per_gym.aggregate.vo.response;

import com.dev5ops.healthtart.exercise_equipment.aggregate.ExerciseEquipment;
import com.dev5ops.healthtart.gym.aggregate.Gym;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseRegisterEquipmentPerGymVO {
    private Long equipmentPerGymCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Gym gym;
    private ExerciseEquipment exerciseEquipment;
}
