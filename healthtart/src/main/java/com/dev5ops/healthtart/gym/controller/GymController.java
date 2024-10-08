package com.dev5ops.healthtart.gym.controller;

import com.dev5ops.healthtart.gym.aggregate.Gym;
import com.dev5ops.healthtart.gym.aggregate.vo.request.RequestRegisterGymVO;
import com.dev5ops.healthtart.gym.aggregate.vo.response.ResponseRegisterGymVO;
import com.dev5ops.healthtart.gym.service.GymService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ResponseRegisterGymVO> registerGym(@RequestBody RequestRegisterGymVO requestRegisterGymVO) {
        ResponseRegisterGymVO registerGym = gymService.registerGym(requestRegisterGymVO);

        return ResponseEntity.status(HttpStatus.CREATED).body(registerGym);
    }
}
