package com.dev5ops.healthtart.gym.aggregate;

import com.dev5ops.healthtart.equipment_per_gym.aggregate.EquipmentPerGym;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "gym")
@Table(name = "gym")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Gym {

    @Id
    @Column(name = "gym_code", nullable = false, unique = true)
    private Long gymCode;

    @Column(name = "gym_name")
    private String gymName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "business_number", nullable = false)
    private String businessNumber;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "gym")
    private List<EquipmentPerGym> equipmentPerGyms;
}
