package com.dev5ops.healthtart.equipment_per_gym.controller;

import com.dev5ops.healthtart.equipment_per_gym.aggregate.vo.request.RequestRegisterEquipmentPerGymVO;
import com.dev5ops.healthtart.equipment_per_gym.aggregate.vo.response.ResponseRegisterEquipmentPerGymVO;
import com.dev5ops.healthtart.equipment_per_gym.dto.EquipmentPerGymDTO;
import com.dev5ops.healthtart.equipment_per_gym.service.EquipmentPerGymService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterEquipmentPerGymVO> registerEquipmentPerGym(@RequestBody RequestRegisterEquipmentPerGymVO request) {
        EquipmentPerGymDTO equipmentPerGymDTO = modelMapper.map(request, EquipmentPerGymDTO.class);
        EquipmentPerGymDTO registeredEquipment = equipmentPerGymService.registerEquipmentPerGym(equipmentPerGymDTO);

        ResponseRegisterEquipmentPerGymVO response = new ResponseRegisterEquipmentPerGymVO(
                registeredEquipment.getEquipmentPerGymCode(),
                registeredEquipment.getCreatedAt(),
                registeredEquipment.getUpdatedAt(),
                registeredEquipment.getGym(),
                registeredEquipment.getExerciseEquipment()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
