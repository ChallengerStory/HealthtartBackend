package com.dev5ops.healthtart.exercise_equipment.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
