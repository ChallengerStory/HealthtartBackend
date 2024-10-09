package com.dev5ops.healthtart.equipment_per_gym.repository;

import com.dev5ops.healthtart.equipment_per_gym.aggregate.EquipmentPerGym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentPerGymRepository extends JpaRepository<EquipmentPerGym, Integer> {
}
