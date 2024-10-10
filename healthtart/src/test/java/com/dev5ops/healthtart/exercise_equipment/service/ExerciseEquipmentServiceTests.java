package com.dev5ops.healthtart.exercise_equipment.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.exercise_equipment.aggregate.ExerciseEquipment;
import com.dev5ops.healthtart.exercise_equipment.aggregate.vo.request.RequestEditEquipmentVO;
import com.dev5ops.healthtart.exercise_equipment.aggregate.vo.request.RequestRegisterEquipmentVO;
import com.dev5ops.healthtart.exercise_equipment.dto.ExerciseEquipmentDTO;
import com.dev5ops.healthtart.exercise_equipment.repository.ExerciseEquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExerciseEquipmentServiceTests {

    @Mock
    private ExerciseEquipmentRepository exerciseEquipmentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ExerciseEquipmentService exerciseEquipmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("운동 기구 등록 성공")
    @Test
    void testRegisterEquipment_Success() {
        // Given
        ExerciseEquipmentDTO request = new ExerciseEquipmentDTO(
                null,
                "testName",
                "testBodyPart",
                "testDescription",
                "testImage",
                "testVideo",
                null,
                null
        );

        ExerciseEquipment mockEquipment = ExerciseEquipment.builder()
                .exerciseEquipmentCode(1L)
                .exerciseEquipmentName("testName")
                .bodyPart("testBodyPart")
                .exerciseDescription("testDescription")
                .exerciseImage("testImage")
                .recommendedVideo("testVideo")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(modelMapper.map(request, ExerciseEquipment.class)).thenReturn(mockEquipment);
        when(exerciseEquipmentRepository.save(any(ExerciseEquipment.class))).thenReturn(mockEquipment);
        when(modelMapper.map(mockEquipment, ExerciseEquipmentDTO.class)).thenReturn(new ExerciseEquipmentDTO(
                1L, "testName", "testBodyPart", "testDescription", "testImage", "testVideo", LocalDateTime.now(), LocalDateTime.now()
        ));

        // When
        ExerciseEquipmentDTO response = exerciseEquipmentService.registerEquipment(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getExerciseEquipmentCode());
        verify(exerciseEquipmentRepository, times(1)).save(any(ExerciseEquipment.class));
    }

    @DisplayName("운동 기구 등록 실패 - 중복된 장비명")
    @Test
    void testRegisterEquipment_Duplicate() {
        // Given
        RequestRegisterEquipmentVO request = new RequestRegisterEquipmentVO(
                "testName",
                "testBodyPart",
                "testDescription",
                "testImage",
                "testVideo"
        );

        ExerciseEquipment existingEquipment = ExerciseEquipment.builder()
                .exerciseEquipmentCode(1L)
                .exerciseEquipmentName("testName")
                .bodyPart("testBodyPart")
                .exerciseDescription("testDescription")
                .exerciseImage("testImage")
                .recommendedVideo("testVideo")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(exerciseEquipmentRepository.findByExerciseEquipmentName(request.getExerciseEquipmentName()))
                .thenReturn(Optional.of(existingEquipment));

        ExerciseEquipmentDTO mockEquipmentDTO = new ExerciseEquipmentDTO(
                null, "testName", "testBodyPart", "testDescription", "testImage", "testVideo", null, null
        );

        when(modelMapper.map(any(RequestRegisterEquipmentVO.class), eq(ExerciseEquipmentDTO.class)))
                .thenReturn(mockEquipmentDTO);

        ExerciseEquipment exerciseEquipment = ExerciseEquipment.builder()
                .exerciseEquipmentCode(1L)
                .exerciseEquipmentName(request.getExerciseEquipmentName())
                .bodyPart(request.getBodyPart())
                .exerciseDescription(request.getExerciseDescription())
                .exerciseImage(request.getExerciseImage())
                .recommendedVideo(request.getRecommendedVideo())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(modelMapper.map(any(ExerciseEquipmentDTO.class), eq(ExerciseEquipment.class))).thenReturn(exerciseEquipment);

        // When & Then
        CommonException exception = assertThrows(CommonException.class, () -> {
            exerciseEquipmentService.registerEquipment(mockEquipmentDTO);
        });

        assertEquals(StatusEnum.EQUIPMENT_DUPLICATE, exception.getStatusEnum());
        verify(exerciseEquipmentRepository, never()).save(any(ExerciseEquipment.class));
    }

    @DisplayName("운동 기구 수정 성공")
    @Test
    void testEditEquipment_Success() {
        // Given
        Long equipmentCode = 1L;
        RequestEditEquipmentVO request = new RequestEditEquipmentVO(
                "updated testName",
                "testBodyPart",
                "updated testDescription",
                "updated testImage",
                "updated testVideo"
        );

        ExerciseEquipment existingEquipment = ExerciseEquipment.builder()
                .exerciseEquipmentCode(equipmentCode)
                .exerciseEquipmentName("testName")
                .bodyPart("testBodyPart")
                .exerciseDescription("testDescription")
                .exerciseImage("testImage")
                .recommendedVideo("testVideo")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(exerciseEquipmentRepository.findById(equipmentCode)).thenReturn(Optional.of(existingEquipment));

        ExerciseEquipment updatedEquipment = ExerciseEquipment.builder()
                .exerciseEquipmentCode(equipmentCode)
                .exerciseEquipmentName(request.getExerciseEquipmentName())
                .bodyPart(request.getBodyPart())
                .exerciseDescription(request.getExerciseDescription())
                .exerciseImage(request.getExerciseImage())
                .recommendedVideo(request.getRecommendedVideo())
                .createdAt(existingEquipment.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(exerciseEquipmentRepository.save(existingEquipment)).thenReturn(updatedEquipment);
        when(modelMapper.map(updatedEquipment, ExerciseEquipmentDTO.class)).thenReturn(new ExerciseEquipmentDTO(
                equipmentCode,
                request.getExerciseEquipmentName(),
                request.getBodyPart(),
                request.getExerciseDescription(),
                request.getExerciseImage(),
                request.getRecommendedVideo(),
                existingEquipment.getCreatedAt(),
                LocalDateTime.now()
        ));

        // When
        ExerciseEquipmentDTO result = exerciseEquipmentService.editEquipment(equipmentCode, request);

        // Then
        assertNotNull(result);
        assertEquals("updated testName", result.getExerciseEquipmentName());
        assertNotNull(result.getUpdatedAt());
        verify(exerciseEquipmentRepository, times(1)).save(existingEquipment);
    }

    @DisplayName("운동 기구 삭제 성공")
    @Test
    void testDeleteEquipment_Success() {
        // Given
        Long equipmentCode = 1L;
        ExerciseEquipment existingEquipment = ExerciseEquipment.builder()
                .exerciseEquipmentCode(equipmentCode)
                .exerciseEquipmentName("testName")
                .bodyPart("testBodyPart")
                .exerciseDescription("testDescription")
                .exerciseImage("testImage")
                .recommendedVideo("testVideo")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(exerciseEquipmentRepository.findById(equipmentCode)).thenReturn(Optional.of(existingEquipment));

        // When
        exerciseEquipmentService.deleteEquipment(equipmentCode);

        // Then
        verify(exerciseEquipmentRepository, times(1)).delete(existingEquipment);
    }

    @DisplayName("운동 기구 삭제 실패 - 장비를 찾을 수 없음")
    @Test
    void testDeleteEquipment_NotFound() {
        // Given
        Long equipmentCode = 1L;

        when(exerciseEquipmentRepository.findById(equipmentCode)).thenReturn(Optional.empty());

        // When & Then
        CommonException exception = assertThrows(CommonException.class, () -> {
            exerciseEquipmentService.deleteEquipment(equipmentCode);
        });

        assertEquals(StatusEnum.EQUIPMENT_NOT_FOUND, exception.getStatusEnum());
        verify(exerciseEquipmentRepository, never()).delete(any(ExerciseEquipment.class));
    }

    @DisplayName("운동 기구 단 건 조회 성공")
    @Test
    void testFindEquipmentByEquipmentCode_Success() {
        // Given
        Long equipmentCode = 1L;
        ExerciseEquipment existingEquipment = ExerciseEquipment.builder()
                .exerciseEquipmentCode(equipmentCode)
                .exerciseEquipmentName("testName")
                .bodyPart("testBodyPart")
                .exerciseDescription("testDescription")
                .exerciseImage("testImage")
                .recommendedVideo("testVideo")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(exerciseEquipmentRepository.findById(equipmentCode)).thenReturn(Optional.of(existingEquipment));

        ExerciseEquipmentDTO mockDTO = new ExerciseEquipmentDTO(
                equipmentCode,
                existingEquipment.getExerciseEquipmentName(),
                existingEquipment.getBodyPart(),
                existingEquipment.getExerciseDescription(),
                existingEquipment.getExerciseImage(),
                existingEquipment.getRecommendedVideo(),
                existingEquipment.getCreatedAt(),
                existingEquipment.getUpdatedAt()
        );

        when(modelMapper.map(existingEquipment, ExerciseEquipmentDTO.class)).thenReturn(mockDTO);

        // When
        ExerciseEquipmentDTO result = exerciseEquipmentService.findEquipmentByEquipmentCode(equipmentCode);

        // Then
        assertNotNull(result);
        assertEquals(equipmentCode, result.getExerciseEquipmentCode());
        verify(exerciseEquipmentRepository, times(1)).findById(equipmentCode);
    }

    @DisplayName("운동 기구 단 건 조회 실패 - 장비를 찾을 수 없음")
    @Test
    void testFindEquipmentByEquipmentCode_NotFound() {
        // Given
        Long equipmentCode = 1L;

        when(exerciseEquipmentRepository.findById(equipmentCode)).thenReturn(Optional.empty());

        // When & Then
        CommonException exception = assertThrows(CommonException.class, () -> {
            exerciseEquipmentService.findEquipmentByEquipmentCode(equipmentCode);
        });

        assertEquals(StatusEnum.EQUIPMENT_NOT_FOUND, exception.getStatusEnum());
        verify(exerciseEquipmentRepository, times(1)).findById(equipmentCode);
    }

    @DisplayName("운동 기구 전체 조회 성공")
    @Test
    void testFindAllEquipment_Success() {
        // Given
        List<ExerciseEquipment> equipmentList = new ArrayList<>();
        equipmentList.add(ExerciseEquipment.builder()
                .exerciseEquipmentCode(1L)
                .exerciseEquipmentName("testName1")
                .bodyPart("testBodyPart1")
                .exerciseDescription("testDescription1")
                .exerciseImage("testImage1")
                .recommendedVideo("testVideo1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        equipmentList.add(ExerciseEquipment.builder()
                .exerciseEquipmentCode(2L)
                .exerciseEquipmentName("testName2")
                .bodyPart("testBodyPart2")
                .exerciseDescription("testDescription2")
                .exerciseImage("testImage2")
                .recommendedVideo("testVideo2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        when(exerciseEquipmentRepository.findAll()).thenReturn(equipmentList);

        when(modelMapper.map(equipmentList.get(0), ExerciseEquipmentDTO.class)).thenReturn(new ExerciseEquipmentDTO(
                1L, "testName1", "testBodyPart1", "testDescription1", "testImage1", "testVideo1", LocalDateTime.now(), LocalDateTime.now()));
        when(modelMapper.map(equipmentList.get(1), ExerciseEquipmentDTO.class)).thenReturn(new ExerciseEquipmentDTO(
                2L, "testName2", "testBodyPart2", "testDescription2", "testImage2", "testVideo2", LocalDateTime.now(), LocalDateTime.now()));

        // When
        List<ExerciseEquipmentDTO> result = exerciseEquipmentService.findAllEquipment();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(exerciseEquipmentRepository, times(1)).findAll();
    }
}
