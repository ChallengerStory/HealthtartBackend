package com.dev5ops.healthtart.recommended_workout_history.service;

import com.dev5ops.healthtart.recommended_workout_history.domain.dto.RecommendedWorkoutHistoryDTO;
import com.dev5ops.healthtart.recommended_workout_history.domain.entity.RecommendedWorkoutHistory;
import com.dev5ops.healthtart.recommended_workout_history.repository.RecommendedWorkoutHistoryRepository;
import com.dev5ops.healthtart.routine.domain.entity.Routine;
import com.dev5ops.healthtart.workoutinfo.domain.entity.WorkoutInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  // Mockito를 사용한 단위 테스트에 적합
class RecommendedWorkoutHistoryServiceImplTests {

    @Mock
    private RecommendedWorkoutHistoryRepository recommendedWorkoutHistoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RecommendedWorkoutHistoryServiceImpl recommendedWorkoutHistoryService;

    private RecommendedWorkoutHistory history1;
    private RecommendedWorkoutHistory history2;
    private WorkoutInfo workoutInfo1;
    private WorkoutInfo workoutInfo2;
    private Routine routine1;
    private Routine routine2;

//    @BeforeEach
//    void setUp() {
//        routine1 = Routine.builder()
//                .routineCode(1L)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        routine2 = Routine.builder()
//                .routineCode(2L)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        workoutInfo1 = WorkoutInfo.builder()
//                .workoutInfoCode(101L)
//                .routineCode(routine1)
//                .title("Workout 1")
//                .time(60)
//                .build();
//
//        workoutInfo2 = WorkoutInfo.builder()
//                .workoutInfoCode(102L)
//                .routineCode(routine2)
//                .title("Workout 2")
//                .time(45)
//                .build();
//
//        history1 = RecommendedWorkoutHistory.builder()
//                .historyCode(1L)
//                .routineRatings(5.0)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .workoutInfoCode(workoutInfo1)
//                .build();
//
//        history2 = RecommendedWorkoutHistory.builder()
//                .historyCode(2L)
//                .routineRatings(3.0)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .workoutInfoCode(workoutInfo2)
//                .build();
//    }
//
//    @Test
//    @DisplayName("만족도 조회 및 평균별 추천 테스트")
//    void testFindByRatingOrder() {
//        when(recommendedWorkoutHistoryRepository.findAll()).thenReturn(Arrays.asList(history1, history2));
//
//        RecommendedWorkoutHistoryDTO dto1 = RecommendedWorkoutHistoryDTO.builder()
//                .historyCode(1L)
//                .routineRatings(4.5)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .workoutInfoCode(workoutInfo1)
//                .build();
//
//        RecommendedWorkoutHistoryDTO dto2 = RecommendedWorkoutHistoryDTO.builder()
//                .historyCode(2L)
//                .routineRatings(3.0)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .workoutInfoCode(workoutInfo2)
//                .build();
//        when(modelMapper.map(history1, RecommendedWorkoutHistoryDTO.class)).thenReturn(dto1);
//        when(modelMapper.map(history2, RecommendedWorkoutHistoryDTO.class)).thenReturn(dto2);
//
//        List<Map.Entry<Long, Double>> result = recommendedWorkoutHistoryService.findByRatingOrder();
//
//        assertEquals(2, result.size());
//        assertEquals(1L, result.get(0).getKey());
//        assertEquals(4.5, result.get(0).getValue());
//        assertEquals(2L, result.get(1).getKey());
//        assertEquals(3.0, result.get(1).getValue());
//    }

}