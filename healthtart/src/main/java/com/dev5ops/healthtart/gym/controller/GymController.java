package com.dev5ops.healthtart.gym.controller;

import com.dev5ops.healthtart.gym.aggregate.vo.request.RequestEditGymVO;
import com.dev5ops.healthtart.gym.aggregate.vo.request.RequestRegisterGymVO;
import com.dev5ops.healthtart.gym.aggregate.vo.response.ResponseEditGymVO;
import com.dev5ops.healthtart.gym.aggregate.vo.response.ResponseRegisterGymVO;
import com.dev5ops.healthtart.gym.dto.GymDTO;
import com.dev5ops.healthtart.gym.service.GymService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("gymController")
@RequestMapping("gym")
@Slf4j
public class GymController {
    private final GymService gymService;
    private final ModelMapper modelMapper;

    public GymController(GymService gymService, ModelMapper modelMapper) {
        this.gymService = gymService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "관리자 - 헬스장 등록")
    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterGymVO> registerGym(@RequestBody RequestRegisterGymVO request) {
        GymDTO gymDTO = modelMapper.map(request, GymDTO.class);
        GymDTO registerGym = gymService.registerGym(gymDTO);

        ResponseRegisterGymVO response = new ResponseRegisterGymVO(
                registerGym.getGymCode(),
                registerGym.getGymName(),
                registerGym.getAddress(),
                registerGym.getBusinessNumber(),
                registerGym.getCreatedAt(),
                registerGym.getUpdatedAt(),
                registerGym.getEquipmentPerGyms()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "관리자 - 헬스장 정보 수정")
    @PatchMapping("/{gymCode}/edit")
    public ResponseEntity<ResponseEditGymVO> editGym(@PathVariable Long gymCode, @RequestBody RequestEditGymVO request) {
        GymDTO updatedGym = gymService.editGym(gymCode, request);
        ResponseEditGymVO response = new ResponseEditGymVO(
                updatedGym.getGymName(),
                updatedGym.getAddress(),
                updatedGym.getBusinessNumber(),
                updatedGym.getUpdatedAt(),
                updatedGym.getEquipmentPerGyms()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "관리자 - 헬스장 정보 삭제")
    @DeleteMapping("/{gymCode}/delete")
    public ResponseEntity<String> deleteGym(@PathVariable Long gymCode) {
        gymService.deleteGym(gymCode);

        return ResponseEntity.ok("헬스장이 성공적으로 삭제되었습니다.");
    }
}
