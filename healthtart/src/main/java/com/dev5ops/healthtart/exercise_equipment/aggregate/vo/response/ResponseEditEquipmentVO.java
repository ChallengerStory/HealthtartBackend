package com.dev5ops.healthtart.exercise_equipment.aggregate.vo.response;

import com.dev5ops.healthtart.equipment_per_gym.aggregate.EquipmentPerGym;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseEditEquipmentVO {
    private String exerciseEquipmentName;
    private String bodyPart;
    private String exerciseDescription;
    private String exerciseImage;
    private String recommendedVideo;
    private LocalDateTime updatedAt;
    private List<EquipmentPerGym> equipmentPerGyms;
}
