package com.dev5ops.healthtart.exercise_equipment.aggregate;

import com.dev5ops.healthtart.equipment_per_gym.aggregate.EquipmentPerGym;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "exercise_equipment")
@Table(name = "exercise_equipment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ExerciseEquipment {

    @Id
    @Column(name = "exercise_equipment_code", nullable = false, unique = true)
    private Long exerciseEquipmentCode;

    @Column(name = "exercise_equipment_name", nullable = false)
    private String exerciseEquipmentName;

    @Column(name = "body_part", nullable = false)
    private String bodyPart;

    @Column(name = "exercise_description", nullable = false)
    private String exerciseDescription;

    @Column(name = "exercise_image")
    private String exerciseImage;

    @Column(name = "recommended_video")
    private String recommendedVideo;

    @OneToMany(mappedBy = "exerciseEquipment")
    private List<EquipmentPerGym> equipmentPerGyms;
}
