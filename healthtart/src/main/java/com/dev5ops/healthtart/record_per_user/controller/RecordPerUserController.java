package com.dev5ops.healthtart.record_per_user.controller;


import com.dev5ops.healthtart.record_per_user.domain.dto.RecordPerUserDTO;
import com.dev5ops.healthtart.record_per_user.domain.vo.vo.request.RequestRegisterRecordPerUserVO;
import com.dev5ops.healthtart.record_per_user.domain.vo.vo.response.ResponseFindPerUserVO;
import com.dev5ops.healthtart.record_per_user.domain.vo.vo.response.ResponseRegisterRecordPerUserVO;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController("recordPerUserController")
@RequestMapping("/recordperuser")
@RequiredArgsConstructor
@Slf4j
public class RecordPerUserController {
    private final RecordPerUserService recordPerUserService;
    private final ModelMapper modelmapper;

    @Operation(summary = "유저 - 유저별 운동기록 조회")
    @GetMapping("/{userCode}")
    public ResponseEntity<List<ResponseFindPerUserVO>> getRecordPerUser(@PathVariable("userCode") String userCode) {
        List<RecordPerUserDTO> recordPerUserDTO = recordPerUserService
                .findRecordByUserCode(userCode);

        List<ResponseFindPerUserVO> response = recordPerUserDTO.stream()
                .map(record -> new ResponseFindPerUserVO(
                        record.getDayOfExercise(),
                        record.getExerciseDuration()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 - 날짜별 운동기록 조회")
    @GetMapping("/{userCode}/{dayOfExercise}")
    public ResponseEntity<List<ResponseFindPerUserVO>> getRecordPerDate(
            @PathVariable("userCode") String userCode, @PathVariable("dayOfExercise") LocalDate dayOfExercise) {
        List<RecordPerUserDTO> recordPerUserDTO = recordPerUserService
                .findRecordPerDate(userCode, dayOfExercise);

        List<ResponseFindPerUserVO> response = recordPerUserDTO.stream()
                .map(record -> new ResponseFindPerUserVO(
                        record.getDayOfExercise(),
                        record.getExerciseDuration()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}
