package com.dev5ops.healthtart.exercise_equipment.dto;

import com.dev5ops.healthtart.equipment_per_gym.aggregate.EquipmentPerGym;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ExerciseEquipmentDTO {

    @JsonProperty("exercise_equipment_code")
    private Long exerciseEquipmentCode;

    @JsonProperty("exercise_equipment_name")
    private String exerciseEquipmentName;

    @JsonProperty("body_part")
    private String bodyPart;

    @JsonProperty("exercise_description")
    private String exerciseDescription;

    @JsonProperty("exercise_image")
    private String exerciseImage;

    @JsonProperty("recommended_video")
    private String recommendedVideo;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("equipment_per_gym")
    private List<EquipmentPerGym> equipmentPerGyms;
}
