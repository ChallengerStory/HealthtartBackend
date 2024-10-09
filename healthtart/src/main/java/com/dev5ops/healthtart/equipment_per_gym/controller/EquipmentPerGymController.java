package com.dev5ops.healthtart.equipment_per_gym.controller;

import com.dev5ops.healthtart.equipment_per_gym.service.EquipmentPerGymService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("equipmentPerGymController")
@RequestMapping("equipmentPerGym")
@Slf4j
public class EquipmentPerGymController {

    private final EquipmentPerGymService equipmentPerGymService;
    private final ModelMapper modelMapper;

    @Autowired
    public EquipmentPerGymController(EquipmentPerGymService equipmentPerGymService, ModelMapper modelMapper) {
        this.equipmentPerGymService = equipmentPerGymService;
        this.modelMapper = modelMapper;
    }
}
