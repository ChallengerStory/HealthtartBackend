package com.dev5ops.healthtart.equipment_per_gym.service;

import com.dev5ops.healthtart.equipment_per_gym.repository.EquipmentPerGymRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service("equipmentPerGymService")
public class EquipmentPerGymService {
    private final EquipmentPerGymRepository equipmentPerGymRepository;
    private final ModelMapper modelMapper;


}
