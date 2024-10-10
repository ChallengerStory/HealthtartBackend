package com.dev5ops.healthtart.exercise_equipment.aggregate.vo.response;

import com.dev5ops.healthtart.equipment_per_gym.aggregate.EquipmentPerGym;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseRegisterEquipmentVO {
    private Long exerciseEquipmentCode;
    private String exerciseEquipmentName;
    private String bodyPart;
    private String exerciseDescription;
    private String exerciseImage;
    private String recommendedVideo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
