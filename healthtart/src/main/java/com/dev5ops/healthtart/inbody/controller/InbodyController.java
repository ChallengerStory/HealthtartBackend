package com.dev5ops.healthtart.inbody.controller;

import com.dev5ops.healthtart.inbody.aggregate.vo.request.RequestEditInbodyVO;
import com.dev5ops.healthtart.inbody.aggregate.vo.request.RequestRegisterInbodyVO;
import com.dev5ops.healthtart.inbody.aggregate.vo.response.ResponseEditInbodyVO;
import com.dev5ops.healthtart.inbody.aggregate.vo.response.ResponseFindInbodyVO;
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

import java.util.ArrayList;
import java.util.List;

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

    @Operation(summary = "관리자 - 인바디 단 건 조회")
    @GetMapping("/{inbodyCode}")
    public ResponseEntity<ResponseFindInbodyVO> getInbody(@PathVariable("inbodyCode") Long inbodyCode) {
        InbodyDTO inbodyDTO = inbodyService.findInbodyByCode(inbodyCode);

        ResponseFindInbodyVO response = new ResponseFindInbodyVO(
                inbodyDTO.getInbodyScore(),
                inbodyDTO.getWeight(),
                inbodyDTO.getHeight(),
                inbodyDTO.getMuscleWeight(),
                inbodyDTO.getFatWeight(),
                inbodyDTO.getBmi(),
                inbodyDTO.getFatPercentage(),
                inbodyDTO.getDayOfInbody(),
                inbodyDTO.getBasalMetabolicRate(),
                inbodyDTO.getUser()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "관리자 - 인바디 전체 조회")
    @GetMapping("/inbody_list")
    public ResponseEntity<List<ResponseFindInbodyVO>> getInbodyList() {
        List<InbodyDTO> inbodyDTOList = inbodyService.findAllInbody();
        List<ResponseFindInbodyVO> responseList = new ArrayList<>();

        for (InbodyDTO inbodyDTO : inbodyDTOList) {
            ResponseFindInbodyVO response = new ResponseFindInbodyVO(
                    inbodyDTO.getInbodyScore(),
                    inbodyDTO.getWeight(),
                    inbodyDTO.getHeight(),
                    inbodyDTO.getMuscleWeight(),
                    inbodyDTO.getFatWeight(),
                    inbodyDTO.getBmi(),
                    inbodyDTO.getFatPercentage(),
                    inbodyDTO.getDayOfInbody(),
                    inbodyDTO.getBasalMetabolicRate(),
                    inbodyDTO.getUser()
            );

            responseList.add(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @Operation(summary = "유저 - 본인의 인바디 단 건 조회")
    @GetMapping("/my-inbody/{inbodyCode}")
    public ResponseEntity<ResponseFindInbodyVO> getUserInbody(@PathVariable("inbodyCode") Long inbodyCode, @RequestParam("userCode") String userCode) {
        InbodyDTO inbodyDTO = inbodyService.findInbodyByCodeAndUser(inbodyCode, userCode);

        ResponseFindInbodyVO response = new ResponseFindInbodyVO(
                inbodyDTO.getInbodyScore(),
                inbodyDTO.getWeight(),
                inbodyDTO.getHeight(),
                inbodyDTO.getMuscleWeight(),
                inbodyDTO.getFatWeight(),
                inbodyDTO.getBmi(),
                inbodyDTO.getFatPercentage(),
                inbodyDTO.getDayOfInbody(),
                inbodyDTO.getBasalMetabolicRate(),
                inbodyDTO.getUser()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "유저 - 본인의 모든 인바디 정보 조회")
    @GetMapping("/my-inbody")
    public ResponseEntity<List<ResponseFindInbodyVO>> getUserInbodyList(@RequestParam("userCode") String userCode) {
        List<InbodyDTO> inbodyDTOList = inbodyService.findAllInbodyByUser(userCode);
        List<ResponseFindInbodyVO> responseList = new ArrayList<>();

        for (InbodyDTO inbodyDTO : inbodyDTOList) {
            ResponseFindInbodyVO response = new ResponseFindInbodyVO(
                    inbodyDTO.getInbodyScore(),
                    inbodyDTO.getWeight(),
                    inbodyDTO.getHeight(),
                    inbodyDTO.getMuscleWeight(),
                    inbodyDTO.getFatWeight(),
                    inbodyDTO.getBmi(),
                    inbodyDTO.getFatPercentage(),
                    inbodyDTO.getDayOfInbody(),
                    inbodyDTO.getBasalMetabolicRate(),
                    inbodyDTO.getUser()
            );
            responseList.add(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

}
