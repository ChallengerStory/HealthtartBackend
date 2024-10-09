package com.dev5ops.healthtart.equipment_per_gym.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.equipment_per_gym.aggregate.EquipmentPerGym;
import com.dev5ops.healthtart.equipment_per_gym.dto.EquipmentPerGymDTO;
import com.dev5ops.healthtart.equipment_per_gym.repository.EquipmentPerGymRepository;
import com.dev5ops.healthtart.exercise_equipment.aggregate.ExerciseEquipment;
import com.dev5ops.healthtart.exercise_equipment.repository.ExerciseEquipmentRepository;
import com.dev5ops.healthtart.gym.aggregate.Gym;
import com.dev5ops.healthtart.gym.repository.GymRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EquipmentPerGymServiceTests {

    @Mock
    private GymRepository gymRepository;

    @Mock
    private ExerciseEquipmentRepository exerciseEquipmentRepository;

    @Mock
    private EquipmentPerGymRepository equipmentPerGymRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EquipmentPerGymService equipmentPerGymService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("운동기구 등록 성공 - 헬스장과 운동기구가 존재")
    @Test
    void testRegisterEquipmentPerGym_Success() {
        // Given
        EquipmentPerGymDTO requestDTO = new EquipmentPerGymDTO(
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new Gym(1L, "TestGym", "TestAddress", "000-00-00000", LocalDateTime.now(), LocalDateTime.now()),
                new ExerciseEquipment(1L, "TestEquipment", "이두", "TestDescription", "TestImage", "TestVideo", LocalDateTime.now(), LocalDateTime.now())
        );

        Gym mockGym = Gym.builder()
                .gymCode(1L)
                .gymName("TestGym")
                .address("TestAddress")
                .businessNumber("000-00-00000")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ExerciseEquipment mockEquipment = ExerciseEquipment.builder()
                .exerciseEquipmentCode(1L)
                .exerciseEquipmentName("TestEquipment")
                .bodyPart("이두")
                .exerciseDescription("TestDescription")
                .exerciseImage("TestImage")
                .recommendedVideo("TestVideo")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        EquipmentPerGym mockEquipmentPerGym = EquipmentPerGym.builder()
                .equipmentPerGymCode(1L)
                .gym(mockGym)
                .exerciseEquipment(mockEquipment)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(gymRepository.findById(1L)).thenReturn(Optional.of(mockGym));
        when(exerciseEquipmentRepository.findById(1L)).thenReturn(Optional.of(mockEquipment));
        when(modelMapper.map(requestDTO, EquipmentPerGym.class)).thenReturn(mockEquipmentPerGym);
        when(equipmentPerGymRepository.save(any(EquipmentPerGym.class))).thenReturn(mockEquipmentPerGym);
        when(modelMapper.map(mockEquipmentPerGym, EquipmentPerGymDTO.class)).thenReturn(requestDTO);

        // When
        EquipmentPerGymDTO responseDTO = equipmentPerGymService.registerEquipmentPerGym(requestDTO);

        // Then
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getGym().getGymCode());
        assertEquals(1L, responseDTO.getExerciseEquipment().getExerciseEquipmentCode());
        verify(equipmentPerGymRepository, times(1)).save(any(EquipmentPerGym.class));
    }

    @DisplayName("운동기구 등록 실패 - 헬스장을 찾을 수 없음")
    @Test
    void testRegisterEquipmentPerGym_GymNotFound() {
        // Given
        EquipmentPerGymDTO requestDTO = new EquipmentPerGymDTO(
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new Gym(1L, "TestGym", "TestAddress", "000-00-00000", LocalDateTime.now(), LocalDateTime.now()),
                new ExerciseEquipment(1L, "TestEquipment", "이두", "TestDescription", "TestImage", "TestVideo", LocalDateTime.now(), LocalDateTime.now())
        );

        when(gymRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        CommonException exception = assertThrows(CommonException.class, () -> {
            equipmentPerGymService.registerEquipmentPerGym(requestDTO);
        });

        assertEquals(StatusEnum.GYM_NOT_FOUND, exception.getStatusEnum());
        verify(equipmentPerGymRepository, never()).save(any(EquipmentPerGym.class));
    }

    @DisplayName("운동기구 등록 실패 - 운동기구를 찾을 수 없음")
    @Test
    void testRegisterEquipmentPerGym_EquipmentNotFound() {
        // Given
        EquipmentPerGymDTO requestDTO = new EquipmentPerGymDTO(
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new Gym(1L, "TestGym", "TestAddress", "000-00-00000", LocalDateTime.now(), LocalDateTime.now()),
                new ExerciseEquipment(1L, "TestEquipment", "이두", "TestDescription", "TestImage", "TestVideo", LocalDateTime.now(), LocalDateTime.now())
        );

        Gym mockGym = Gym.builder()
                .gymCode(1L)
                .gymName("TestGym")
                .address("TestAddress")
                .businessNumber("000-00-00000")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(gymRepository.findById(1L)).thenReturn(Optional.of(mockGym));
        when(exerciseEquipmentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        CommonException exception = assertThrows(CommonException.class, () -> {
            equipmentPerGymService.registerEquipmentPerGym(requestDTO);
        });

        assertEquals(StatusEnum.EQUIPMENT_NOT_FOUND, exception.getStatusEnum());
        verify(equipmentPerGymRepository, never()).save(any(EquipmentPerGym.class));
    }
}
