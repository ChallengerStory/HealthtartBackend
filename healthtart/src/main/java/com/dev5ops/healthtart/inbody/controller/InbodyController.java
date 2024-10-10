package com.dev5ops.healthtart.inbody.controller;

import com.dev5ops.healthtart.inbody.aggregate.vo.request.RequestRegisterInbodyVO;
import com.dev5ops.healthtart.inbody.aggregate.vo.response.ResponseRegisterInbodyVO;
import com.dev5ops.healthtart.inbody.dto.InbodyDTO;
import com.dev5ops.healthtart.inbody.service.InbodyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("inbodyController")
@RequestMapping("inbody")
@Slf4j
public class InbodyController {
    private final InbodyService inbodyService;
    private final ModelMapper modelMapper;

    @Autowired
    public InbodyController(InbodyService inbodyService, ModelMapper modelMapper) {
        this.inbodyService = inbodyService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "관리자, 유저 - 인바디 등록")
    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterInbodyVO> registerInbody(@RequestBody RequestRegisterInbodyVO request) {
        InbodyDTO inbodyDTO = modelMapper.map(request, InbodyDTO.class);
        InbodyDTO registerInbody = inbodyService.registerInbody(inbodyDTO);

        ResponseRegisterInbodyVO response = new ResponseRegisterInbodyVO(
                registerInbody.getInbodyCode(),
                registerInbody.getInbodyScore(),
                registerInbody.getWeight(),
                registerInbody.getHeight(),
                registerInbody.getMuscleWeight(),
                registerInbody.getFatWeight(),
                registerInbody.getBmi(),
                registerInbody.getFatPercentage(),
                registerInbody.getDayOfInbody(),
                registerInbody.getBasalMetabolicRate(),
                registerInbody.getCreatedAt(),
                registerInbody.getUpdatedAt(),
                registerInbody.getUser()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
