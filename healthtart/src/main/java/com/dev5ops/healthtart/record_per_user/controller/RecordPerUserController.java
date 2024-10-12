package com.dev5ops.healthtart.record_per_user.controller;


import com.dev5ops.healthtart.record_per_user.domain.dto.RecordPerUserDTO;
import com.dev5ops.healthtart.record_per_user.domain.vo.vo.response.ResponseFindPerUserVO;
import com.dev5ops.healthtart.record_per_user.service.RecordPerUserService;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController("recordPerUserController")
@RequestMapping("/recordperuser")
@RequiredArgsConstructor
@Slf4j
public class RecordPerUserController {
    private final RecordPerUserService recordPerUserService;
    private final ModelMapper modelmapper;

    @Operation(summary = "유저 - 유저별 운동기록 조회")
    @GetMapping("/{userCode}")
    public ResponseEntity<List<ResponseFindPerUserVO>> getRecordPerUser(@PathVariable("userCode") UserEntity userCode) {
        RecordPerUserDTO recordPerUserDTO = (RecordPerUserDTO) recordPerUserService
                .findRecordByUserCode(userCode);

        ResponseFindPerUserVO response = new ResponseFindPerUserVO(
                recordPerUserDTO.getDayOfExercise(),
                recordPerUserDTO.getExerciseDuration()
        );

        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonList(response));
    }

    @Operation(summary = "유저 - 날짜별 운동기록 조회")
    @GetMapping("/{userCode}/{dayOfExercise}")
    public ResponseEntity<List<ResponseFindPerUserVO>> getRecordPerDate(
            @PathVariable("userCode") UserEntity userCode, @PathVariable("dayOfExercise") LocalDate dayOfExercise) {
        RecordPerUserDTO recordPerUserDTO = (RecordPerUserDTO) recordPerUserService
                .findRecordPerDate(userCode, dayOfExercise);

        ResponseFindPerUserVO response = new ResponseFindPerUserVO(
                recordPerUserDTO.getDayOfExercise(),
                recordPerUserDTO.getExerciseDuration()
        );

        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonList(response));
    }

//    public ResponseEntity<ResponseRegisterRecordPerUserVO> registerRecordPerUser(
//            @RequestBody RequestRegisterRecordPerUserVO request) {
//        RecordPerUserDTO response = recordPerUserService
//                .registerRecordPerUser(modelmapper.map(request, RecordPerUserDTO.class));
//
//        )
//    }
}
