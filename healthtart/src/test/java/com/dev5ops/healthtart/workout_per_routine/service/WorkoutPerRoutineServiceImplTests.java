package com.dev5ops.healthtart.workout_per_routine.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.workout_per_routine.domain.dto.WorkoutPerRoutineDTO;
import com.dev5ops.healthtart.workout_per_routine.domain.entity.WorkoutPerRoutine;
import com.dev5ops.healthtart.workout_per_routine.domain.vo.vo.*;
import com.dev5ops.healthtart.workout_per_routine.repository.WorkoutPerRoutineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutPerRoutineServiceImplTests {

    @InjectMocks
    private WorkoutPerRoutineServiceImpl workoutPerRoutineService;

    @Mock
    private WorkoutPerRoutineRepository workoutPerRoutineRepository;

    @Mock
    private ModelMapper modelMapper;

    private WorkoutPerRoutine mockRoutine;

    @BeforeEach
    void setUp() {
        mockRoutine = WorkoutPerRoutine.builder()
                .workoutPerRoutineCode(1L)
                .workoutOrder(1)
                .weightSet(3)
                .numberPerSet(12)
                .weightPerSet(60)
                .workoutTime(30)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .recordCode(100L)
                .exerciseEquipmentCode(200L)
                .build();
    }

    @Test
    @DisplayName("전체 운동 루틴 조회 성공 테스트")
    void testGetWorkoutPerRoutines() {
        // Given
        List<WorkoutPerRoutine> mockRoutines = new ArrayList<>();
        mockRoutines.add(mockRoutine);

        when(workoutPerRoutineRepository.findAll()).thenReturn(mockRoutines);
        ResponseFindWorkoutPerRoutineVO mappedVO = new ResponseFindWorkoutPerRoutineVO();
        when(modelMapper.map(mockRoutine, ResponseFindWorkoutPerRoutineVO.class)).thenReturn(mappedVO);

        // When
        List<ResponseFindWorkoutPerRoutineVO> result = workoutPerRoutineService.getWorkoutPerRoutines();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(workoutPerRoutineRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(mockRoutine, ResponseFindWorkoutPerRoutineVO.class);
    }

    @Test
    @DisplayName("전체 운동 루틴 조회 실패 테스트 - 루틴 없음")
    void testGetWorkoutPerRoutines_RoutineNotFound() {
        // Given
        when(workoutPerRoutineRepository.findAll()).thenReturn(new ArrayList<>());

        // When & Then
        assertThrows(CommonException.class, () -> workoutPerRoutineService.getWorkoutPerRoutines());
    }

    @Test
    @DisplayName("운동 루틴 코드로 조회 성공 테스트")
    void testFindWorkoutPerRoutineByCode() {
        // Given
        when(workoutPerRoutineRepository.findById(anyLong())).thenReturn(Optional.of(mockRoutine));
        ResponseFindWorkoutPerRoutineVO mappedVO = new ResponseFindWorkoutPerRoutineVO();
        when(modelMapper.map(mockRoutine, ResponseFindWorkoutPerRoutineVO.class)).thenReturn(mappedVO);

        // When
        ResponseFindWorkoutPerRoutineVO result = workoutPerRoutineService.findWorkoutPerRoutineByCode(1L);

        // Then
        assertNotNull(result);
        verify(workoutPerRoutineRepository, times(1)).findById(anyLong());
        verify(modelMapper, times(1)).map(mockRoutine, ResponseFindWorkoutPerRoutineVO.class);
    }

    @Test
    @DisplayName("운동 루틴 코드로 조회 실패 테스트 - 루틴 없음")
    void testFindWorkoutPerRoutineByCode_RoutineNotFound() {
        // Given
        when(workoutPerRoutineRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CommonException.class, () -> workoutPerRoutineService.findWorkoutPerRoutineByCode(1L));
    }

    @Test
    @DisplayName("운동 루틴 등록 성공 테스트")
    void testRegisterWorkoutPerRoutine() {
        // Given
        WorkoutPerRoutineDTO workoutPerRoutineDTO = new WorkoutPerRoutineDTO();
        workoutPerRoutineDTO.setWorkoutOrder(1);
        workoutPerRoutineDTO.setWorkoutTime(60);
        workoutPerRoutineDTO.setWeightSet(3);
        workoutPerRoutineDTO.setNumberPerSet(10);
        workoutPerRoutineDTO.setWeightPerSet(20);
        workoutPerRoutineDTO.setExerciseEquipmentCode(1L);
        workoutPerRoutineDTO.setRecordCode(1L);

        when(modelMapper.map(workoutPerRoutineDTO, WorkoutPerRoutine.class)).thenReturn(mockRoutine);
        when(workoutPerRoutineRepository.save(any(WorkoutPerRoutine.class))).thenReturn(mockRoutine);
        when(modelMapper.map(mockRoutine, ResponseInsertWorkoutPerRoutineVO.class)).thenReturn(new ResponseInsertWorkoutPerRoutineVO());

        // When
        ResponseInsertWorkoutPerRoutineVO result = workoutPerRoutineService.registerWorkoutPerRoutine(workoutPerRoutineDTO);

        // Then
        assertNotNull(result);
        verify(workoutPerRoutineRepository, times(1)).save(any(WorkoutPerRoutine.class));
        verify(modelMapper, times(1)).map(workoutPerRoutineDTO, WorkoutPerRoutine.class);
        verify(modelMapper, times(1)).map(mockRoutine, ResponseInsertWorkoutPerRoutineVO.class);
    }

    @Test
    @DisplayName("운동 루틴 수정 성공 테스트")
    void testModifyWorkoutPerRoutine() {
        // Given
        when(workoutPerRoutineRepository.findById(anyLong())).thenReturn(Optional.of(mockRoutine));

        EditWorkoutPerRoutineVO modifyRoutine = new EditWorkoutPerRoutineVO();
        when(workoutPerRoutineRepository.findById(any(Long.class))).thenReturn(Optional.of(mockRoutine));
        doNothing().when(mockRoutine).toUpdate(modifyRoutine);

        when(workoutPerRoutineRepository.save(mockRoutine)).thenReturn(mockRoutine);
        when(modelMapper.map(mockRoutine, ResponseModifyWorkoutPerRoutineVO.class)).thenReturn(new ResponseModifyWorkoutPerRoutineVO());

        // When
        ResponseModifyWorkoutPerRoutineVO result = workoutPerRoutineService.modifyWorkoutPerRoutine(1L, modifyRoutine);

        // Then
        assertNotNull(result);
        verify(workoutPerRoutineRepository, times(1)).findById(anyLong());
        verify(workoutPerRoutineRepository, times(1)).save(mockRoutine);
        verify(modelMapper, times(1)).map(mockRoutine, ResponseModifyWorkoutPerRoutineVO.class);
    }

    @Test
    @DisplayName("운동 루틴 삭제 성공 테스트")
    void testDeleteWorkoutPerRoutine() {
        // Given
        when(workoutPerRoutineRepository.findById(anyLong())).thenReturn(Optional.of(mockRoutine));

        // When
        ResponseDeleteWorkoutPerRoutineVO result = workoutPerRoutineService.deleteWorkoutPerRoutine(1L);

        // Then
        assertNotNull(result);
        verify(workoutPerRoutineRepository, times(1)).findById(anyLong());
        verify(workoutPerRoutineRepository, times(1)).delete(mockRoutine);
    }
}
