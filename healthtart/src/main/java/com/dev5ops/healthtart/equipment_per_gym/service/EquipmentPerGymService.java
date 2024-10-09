package com.dev5ops.healthtart.equipment_per_gym.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.equipment_per_gym.aggregate.EquipmentPerGym;
import com.dev5ops.healthtart.equipment_per_gym.aggregate.vo.request.RequestRegisterEquipmentPerGymVO;
import com.dev5ops.healthtart.equipment_per_gym.dto.EquipmentPerGymDTO;
import com.dev5ops.healthtart.equipment_per_gym.repository.EquipmentPerGymRepository;
import com.dev5ops.healthtart.exercise_equipment.aggregate.ExerciseEquipment;
import com.dev5ops.healthtart.exercise_equipment.repository.ExerciseEquipmentRepository;
import com.dev5ops.healthtart.gym.aggregate.Gym;
import com.dev5ops.healthtart.gym.repository.GymRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("equipmentPerGymService")
@RequiredArgsConstructor
@Slf4j
public class EquipmentPerGymService {
    private final EquipmentPerGymRepository equipmentPerGymRepository;
    private final ModelMapper modelMapper;
    private final GymRepository gymRepository;
    private final ExerciseEquipmentRepository exerciseEquipmentRepository;

    @Transactional
    public EquipmentPerGymDTO registerEquipmentPerGym(EquipmentPerGymDTO equipmentPerGymDTO) {
        Gym gym = gymRepository.findById(equipmentPerGymDTO.getGym().getGymCode()).orElseThrow(() -> new CommonException(StatusEnum.GYM_NOT_FOUND));
        ExerciseEquipment exerciseEquipment = exerciseEquipmentRepository.findById(equipmentPerGymDTO.getExerciseEquipment().getExerciseEquipmentCode()).orElseThrow(() -> new CommonException(StatusEnum.EQUIPMENT_NOT_FOUND));

        EquipmentPerGym equipmentPerGym = modelMapper.map(equipmentPerGymDTO, EquipmentPerGym.class);
        equipmentPerGym.setGym(gym);
        equipmentPerGym.setExerciseEquipment(exerciseEquipment);
        equipmentPerGym.setCreatedAt(LocalDateTime.now());
        equipmentPerGym.setUpdatedAt(LocalDateTime.now());

        EquipmentPerGym savedEquipmentPerGym = equipmentPerGymRepository.save(equipmentPerGym);

        return modelMapper.map(savedEquipmentPerGym, EquipmentPerGymDTO.class);
    }
}

