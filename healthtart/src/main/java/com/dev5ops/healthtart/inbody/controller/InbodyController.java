package com.dev5ops.healthtart.inbody.controller;

import com.dev5ops.healthtart.inbody.aggregate.vo.request.RequestEditInbodyVO;
import com.dev5ops.healthtart.inbody.aggregate.vo.request.RequestRegisterInbodyVO;
import com.dev5ops.healthtart.inbody.aggregate.vo.response.ResponseEditInbodyVO;
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

    @Operation(summary = "관리자, 유저 - 인바디 수정")
    @PostMapping("/{inbodyCode}/edit")
    public ResponseEntity<ResponseEditInbodyVO> editInbody(@PathVariable("inbodyCode") Long inbodyCode, @RequestBody RequestEditInbodyVO request) {
        InbodyDTO inbodyDTO = inbodyService.editInbody(inbodyCode, request);

        ResponseEditInbodyVO response = new ResponseEditInbodyVO(
                inbodyDTO.getInbodyScore(),
                inbodyDTO.getWeight(),
                inbodyDTO.getHeight(),
                inbodyDTO.getMuscleWeight(),
                inbodyDTO.getFatWeight(),
                inbodyDTO.getBmi(),
                inbodyDTO.getFatPercentage(),
                inbodyDTO.getDayOfInbody(),
                inbodyDTO.getBasalMetabolicRate(),
                inbodyDTO.getUpdatedAt(),
                inbodyDTO.getUser()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "관리자, 유저 - 인바디 삭제")
    @DeleteMapping("/{inbodyCode}")
    public ResponseEntity<String> deleteInbody(@PathVariable("inbodyCode") Long inbodyCode) {
        inbodyService.deleteInbody(inbodyCode);

        return ResponseEntity.ok("인바디 정보가 성공적으로 삭제되었습니다.");
    }
}
