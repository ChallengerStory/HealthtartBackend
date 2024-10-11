package com.dev5ops.healthtart.equipment_per_gym.repository;

import com.dev5ops.healthtart.equipment_per_gym.domain.entity.EquipmentPerGym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentPerGymRepository extends JpaRepository<EquipmentPerGym, Long> {
    List<EquipmentPerGym> findByExerciseEquipment_BodyPart(String bodyPart);
}
