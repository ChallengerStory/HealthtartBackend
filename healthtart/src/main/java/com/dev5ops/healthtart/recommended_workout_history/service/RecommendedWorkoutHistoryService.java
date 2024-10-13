package com.dev5ops.healthtart.recommended_workout_history.service;

import com.dev5ops.healthtart.recommended_workout_history.domain.dto.RecommendedWorkoutHistoryDTO;
import com.dev5ops.healthtart.recommended_workout_history.domain.entity.RecommendedWorkoutHistory;
import com.dev5ops.healthtart.recommended_workout_history.repository.RecommendedWorkoutHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service("recommendedWorkoutHistoryService")
public class RecommendedWorkoutHistoryService {
    private final RecommendedWorkoutHistoryRepository recommendedWorkoutHistoryRepository;
    private final ModelMapper modelMapper;

    // 운동추천내역 만족도순 조회 1. 운동루틴별 운동추천내역 조회 2. 만족도만 다 더해서 평균내기
    public List<Map.Entry<Long, Double>> findByRatingOrder() {

        List<RecommendedWorkoutHistory> recommendedWorkoutHistory =
                recommendedWorkoutHistoryRepository.findAll();

        List<RecommendedWorkoutHistoryDTO> recommendedWorkoutHistoryDTOList =
                recommendedWorkoutHistory.stream()
                        .map(history -> modelMapper.map(history, RecommendedWorkoutHistoryDTO.class))
                        .collect(Collectors.toList());

        Map<Long, Double> averageRatingsByRoutineCode = recommendedWorkoutHistoryDTOList.stream()
                .collect(Collectors.groupingBy(
                        RecommendedWorkoutHistoryDTO::getRoutineCode,
                        Collectors.averagingDouble(RecommendedWorkoutHistoryDTO::getRoutineRatings)
                ));

        return averageRatingsByRoutineCode.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

    public RecommendedWorkoutHistoryDTO registerRating
            (RecommendedWorkoutHistoryDTO recommendedWorkoutHistoryDTO){
        RecommendedWorkoutHistory recommendedWorkoutHistory = modelMapper
                .map(recommendedWorkoutHistoryDTO, RecommendedWorkoutHistory.class);

        recommendedWorkoutHistory = recommendedWorkoutHistoryRepository.save(recommendedWorkoutHistory);

        return modelMapper.map(recommendedWorkoutHistory, RecommendedWorkoutHistoryDTO.class);
    }
}



