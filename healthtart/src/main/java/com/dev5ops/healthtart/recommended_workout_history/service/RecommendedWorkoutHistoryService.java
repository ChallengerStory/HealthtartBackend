package com.dev5ops.healthtart.recommended_workout_history.service;

import com.dev5ops.healthtart.recommended_workout_history.domain.dto.RecommendedWorkoutHistoryDTO;
import com.dev5ops.healthtart.recommended_workout_history.domain.entity.RecommendedWorkoutHistory;
import com.dev5ops.healthtart.recommended_workout_history.domain.vo.request.RequestRegisterRecommendedWorkoutHistoryVO;
import com.dev5ops.healthtart.recommended_workout_history.repository.RecommendedWorkoutHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service("recommendedWorkoutHistoryService")
public class RecommendedWorkoutHistoryService {
    private final RecommendedWorkoutHistoryRepository recommendedWorkoutHistoryRepository;
    private final ModelMapper modelMapper;

    // 만족도 등록
    public RecommendedWorkoutHistoryDTO registerRating
            (RequestRegisterRecommendedWorkoutHistoryVO requestRegisterRecommendedWorkoutHistoryVO){
        RecommendedWorkoutHistory recommendedWorkoutHistory = modelMapper
                .map(requestRegisterRecommendedWorkoutHistoryVO, RecommendedWorkoutHistory.class);
        recommendedWorkoutHistory = recommendedWorkoutHistoryRepository.save(recommendedWorkoutHistory);
        return modelMapper.map(recommendedWorkoutHistory, RecommendedWorkoutHistoryDTO.class);
    }
}



