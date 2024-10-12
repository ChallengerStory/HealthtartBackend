package com.dev5ops.healthtart.recommended_workout_history.controller;

import com.dev5ops.healthtart.recommended_workout_history.domain.dto.RecommendedWorkoutHistoryDTO;
import com.dev5ops.healthtart.recommended_workout_history.domain.vo.request.RequestRegisterRecommendedWorkoutHistoryVO;
import com.dev5ops.healthtart.recommended_workout_history.domain.vo.response.ResponseRegisterRecommendedWorkoutHistoryVO;
import com.dev5ops.healthtart.recommended_workout_history.service.RecommendedWorkoutHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("recommendedWorkoutHistoryController")
@RequestMapping("/history")
@Slf4j
@RequiredArgsConstructor
public class RecommendedWorkoutHistoryController {

    private final RecommendedWorkoutHistoryService recommendedWorkoutHistoryService;
    private final ModelMapper modelMapper;

    @Operation(summary = "유저 - 만족도 등록")
    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterRecommendedWorkoutHistoryVO> registerRating
            (@RequestBody RequestRegisterRecommendedWorkoutHistoryVO request){
        RecommendedWorkoutHistoryDTO recommendedWorkoutHistoryDTO = modelMapper
                .map(request, RecommendedWorkoutHistoryDTO.class);
        RecommendedWorkoutHistoryDTO registerRating = recommendedWorkoutHistoryService
                .registerRating(recommendedWorkoutHistoryDTO);

        ResponseRegisterRecommendedWorkoutHistoryVO response = new ResponseRegisterRecommendedWorkoutHistoryVO(
                registerRating.getHistoryCode(),
                registerRating.getRoutineRatings(),
                registerRating.getRecommendedTime(),
                registerRating.getCreatedAt(),
                registerRating.getUpdatedAt(),
                registerRating.getRoutineCode()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
