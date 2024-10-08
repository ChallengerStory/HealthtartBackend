package com.dev5ops.healthtart.gym.dto;

import com.dev5ops.healthtart.equipment_per_gym.dto.EquipmentPerGymDTO;
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
public class GymDTO {

    @JsonProperty("gym_code")
    private Long gymCode;

    @JsonProperty("gym_name")
    private String gymName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("equipment_per_gym")
    private List<EquipmentPerGymDTO> equipmentPerGyms;
}
