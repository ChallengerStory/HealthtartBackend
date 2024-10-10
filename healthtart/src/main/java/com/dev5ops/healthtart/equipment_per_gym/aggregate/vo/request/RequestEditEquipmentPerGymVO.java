package com.dev5ops.healthtart.equipment_per_gym.aggregate.vo.request;

import com.dev5ops.healthtart.exercise_equipment.aggregate.ExerciseEquipment;
import com.dev5ops.healthtart.gym.aggregate.Gym;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestEditEquipmentPerGymVO {
    private Gym gym;
    private ExerciseEquipment exerciseEquipment;
}
