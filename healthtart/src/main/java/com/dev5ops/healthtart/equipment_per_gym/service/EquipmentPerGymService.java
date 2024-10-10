package com.dev5ops.healthtart.equipment_per_gym.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.equipment_per_gym.aggregate.EquipmentPerGym;
import com.dev5ops.healthtart.equipment_per_gym.aggregate.vo.request.RequestEditEquipmentPerGymVO;
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
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public EquipmentPerGymDTO editEquipmentPerGym(Long equipmentPerGymCode, RequestEditEquipmentPerGymVO request) {
        Gym gym = gymRepository.findById(request.getGym().getGymCode()).orElseThrow(() -> new CommonException(StatusEnum.GYM_NOT_FOUND));
        ExerciseEquipment exerciseEquipment = exerciseEquipmentRepository.findById(request.getExerciseEquipment().getExerciseEquipmentCode()).orElseThrow(() -> new CommonException(StatusEnum.EQUIPMENT_NOT_FOUND));

        EquipmentPerGym equipmentPerGym = equipmentPerGymRepository.findById(equipmentPerGymCode).orElseThrow(() -> new CommonException(StatusEnum.EQUIPMENT_PER_GYM_NOT_FOUND));

        equipmentPerGym.setGym(gym);
        equipmentPerGym.setExerciseEquipment(exerciseEquipment);
        equipmentPerGym.setUpdatedAt(LocalDateTime.now());

        EquipmentPerGym savedEquipmentPerGym = equipmentPerGymRepository.save(equipmentPerGym);

        return modelMapper.map(savedEquipmentPerGym, EquipmentPerGymDTO.class);
    }

    @Transactional
    public void deleteEquipmentPerGym(Long equipmentPerGymCode) {
        EquipmentPerGym equipmentPerGym = equipmentPerGymRepository.findById(equipmentPerGymCode).get();

        gymRepository.findById(equipmentPerGym.getGym().getGymCode()).orElseThrow(() -> new CommonException(StatusEnum.GYM_NOT_FOUND));
        exerciseEquipmentRepository.findById(equipmentPerGym.getExerciseEquipment().getExerciseEquipmentCode()).orElseThrow(() -> new CommonException(StatusEnum.EQUIPMENT_NOT_FOUND));

        equipmentPerGymRepository.delete(equipmentPerGym);
    }

    public EquipmentPerGymDTO findEquipmentPerGymByCode(Long equipmentPerGymCode) {
        EquipmentPerGym equipmentPerGym = equipmentPerGymRepository.findById(equipmentPerGymCode).orElseThrow(() -> new CommonException(StatusEnum.EQUIPMENT_PER_GYM_NOT_FOUND));

        return modelMapper.map(equipmentPerGym, EquipmentPerGymDTO.class);
    }

    public List<EquipmentPerGymDTO> findAllEquipmentPer() {
        List<EquipmentPerGym> equipmentPerGyms = equipmentPerGymRepository.findAll();

        return equipmentPerGyms.stream()
                .map(equipmentPerGym -> modelMapper.map(equipmentPerGym, EquipmentPerGymDTO.class))
                .collect(Collectors.toList());
    }
}