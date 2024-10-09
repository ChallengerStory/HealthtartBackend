package com.dev5ops.healthtart.exercise_equipment.aggregate.vo.response;

import com.dev5ops.healthtart.equipment_per_gym.aggregate.EquipmentPerGym;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseFindEquipmentVO {
    private String exerciseEquipmentName;
    private String bodyPart;
    private String exerciseDescription;
    private String exerciseImage;
    private String recommendedVideo;
}
