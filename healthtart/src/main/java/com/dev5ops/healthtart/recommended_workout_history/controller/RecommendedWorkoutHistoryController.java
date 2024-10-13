package com.dev5ops.healthtart.recommended_workout_history.controller;

import com.dev5ops.healthtart.recommended_workout_history.domain.dto.RecommendedWorkoutHistoryDTO;
import com.dev5ops.healthtart.recommended_workout_history.domain.vo.request.RequestRegisterRecommendedWorkoutHistoryVO;
import com.dev5ops.healthtart.recommended_workout_history.domain.vo.response.ResponseFindByRatingOrderVO;
import com.dev5ops.healthtart.recommended_workout_history.domain.vo.response.ResponseRegisterRecommendedWorkoutHistoryVO;
import com.dev5ops.healthtart.recommended_workout_history.service.RecommendedWorkoutHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController("recommendedWorkoutHistoryController")
@RequestMapping("/history")
@Slf4j
@RequiredArgsConstructor
public class RecommendedWorkoutHistoryController {

    private final RecommendedWorkoutHistoryService recommendedWorkoutHistoryService;
    private final ModelMapper modelMapper;

    @Operation(summary = "유저 - 운동루틴별 만족도 내림차순 조회")
    @GetMapping("/ratings")
    public ResponseEntity<List<ResponseFindByRatingOrderVO>> getRating() {
        List<Map.Entry<Long, Double>> ratingList = recommendedWorkoutHistoryService.findByRatingOrder();

        List <ResponseFindByRatingOrderVO> responseList = ratingList.stream()
                .map(entry -> new ResponseFindByRatingOrderVO(entry.getKey(),entry.getValue()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

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
                registerRating.getCreatedAt(),
                registerRating.getUpdatedAt(),
                registerRating.getRoutineCode()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
