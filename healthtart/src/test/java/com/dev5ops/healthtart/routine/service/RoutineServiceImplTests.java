package com.dev5ops.healthtart.routine.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.routine.domain.dto.RoutineDTO;
import com.dev5ops.healthtart.routine.domain.entity.Routine;
import com.dev5ops.healthtart.routine.domain.vo.EditRoutineVO;
import com.dev5ops.healthtart.routine.domain.vo.ResponseDeleteRoutineVO;
import com.dev5ops.healthtart.routine.domain.vo.ResponseInsertRoutineVO;
import com.dev5ops.healthtart.routine.domain.vo.ResponseModifyRoutineVO;
import com.dev5ops.healthtart.routine.domain.vo.ResponseFindRoutineVO;
import com.dev5ops.healthtart.routine.repository.RoutineRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoutineServiceImplTests {

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoutineServiceImpl routineServiceImpl;

    private RoutineDTO routineDTO;
    private Routine routine;

    @BeforeEach
    void setUp() {
        routineDTO = new RoutineDTO();
        routineDTO.setRoutineCode(1L);
        routineDTO.setTitle("김정은도 10kg 감량한 모닝 루틴");
        routineDTO.setTime(30);
        routineDTO.setLink("http://healthtart.com");
        routineDTO.setRecommendMusic("Dynamite - 방탄소년단 (BTS)");

        routine = Routine.builder()
                .routineCode(1L)
                .title("김정은도 10kg 감량한 모닝 루틴")
                .time(30)
                .link("http://healthtart.com")
                .recommendMusic("Dynamite - 방탄소년단 (BTS)")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

    }

    @Test
    @Transactional
    @DisplayName("운동루틴 전체 조회 테스트")
    void getRoutinesTest() {
        when(routineRepository.findAll()).thenReturn(Arrays.asList(routine));
        when(modelMapper.map(routine, ResponseFindRoutineVO.class)).thenReturn(new ResponseFindRoutineVO());

        List<ResponseFindRoutineVO> result = routineServiceImpl.getRoutines();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("김정은도 10kg 감량한 모닝 루틴", result.get(0).getTitle());
    }

    @Test
    @Transactional
    @DisplayName("운동루틴 전체 조회 테스트 - 루틴이 없을 때 예외 발생")
    void getRoutines_ThrowExceptionTest() {
        when(routineRepository.findAll()).thenReturn(Arrays.asList());

        assertThrows(CommonException.class, () -> routineServiceImpl.getRoutines());
    }

    @Test
    @Transactional
    @DisplayName("운동루틴 단일 조회 테스트")
    void findRoutineByCodeTest() {
        when(routineRepository.findById(any(Long.class))).thenReturn(Optional.of(routine));
        when(modelMapper.map(routine, ResponseFindRoutineVO.class)).thenReturn(new ResponseFindRoutineVO());

        ResponseFindRoutineVO result = routineServiceImpl.findRoutineByCode(1L);

        assertNotNull(result);
        assertEquals("김정은도 10kg 감량한 모닝 루틴", result.getTitle());
    }

    @Test
    @Transactional
    @DisplayName("운동루틴 등록 테스트")
    void registerRoutineTest() {
        when(modelMapper.map(any(RoutineDTO.class), eq(Routine.class))).thenReturn(routine);
        when(routineRepository.save(any(Routine.class))).thenReturn(routine);
        when(modelMapper.map(routine, ResponseInsertRoutineVO.class)).thenReturn(new ResponseInsertRoutineVO());

        ResponseInsertRoutineVO result = routineServiceImpl.registerRoutine(routineDTO);

        assertNotNull(result);
        verify(routineRepository, times(1)).save(any(Routine.class));
    }

    @Test
    @Transactional
    @DisplayName("운동루틴 등록 테스트 - 유효하지 않은 DTO로 등록 시 예외 발생")
    void registerRoutine_ThrowExceptionTest() {
        routineDTO.setTitle("");

        assertThrows(CommonException.class, () -> routineServiceImpl.registerRoutine(routineDTO));
    }


    @Test
    @Transactional
    @DisplayName("운동루틴 단일 조회 테스트 - 루틴이 없을 때 예외 발생")
    void findRoutineByCode_ThrowExceptionTest() {
        when(routineRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(CommonException.class, () -> routineServiceImpl.findRoutineByCode(1L));
    }

    @Test
    @Transactional
    @DisplayName("운동루틴 수정 테스트")
    void modifyRoutineTest() {
        EditRoutineVO editRoutineVO = new EditRoutineVO();

        when(routineRepository.findById(any(Long.class))).thenReturn(Optional.of(routine));
        doNothing().when(routine).toUpdate(editRoutineVO);
        when(routineRepository.save(any(Routine.class))).thenReturn(routine);
        when(modelMapper.map(routine, ResponseModifyRoutineVO.class)).thenReturn(new ResponseModifyRoutineVO());

        ResponseModifyRoutineVO result = routineServiceImpl.modifyRoutine(1L, editRoutineVO);

        assertNotNull(result);
        verify(routineRepository, times(1)).save(any(Routine.class));
    }

    @Test
    @Transactional
    @DisplayName("운동루틴 수정 테스트 - 루틴이 없을 때 예외 발생")
    void modifyRoutine_ThrowExceptionTest() {
        when(routineRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(CommonException.class, () -> routineServiceImpl.modifyRoutine(1L, new EditRoutineVO()));
    }

    @Test
    @Transactional
    @DisplayName("운동루틴 삭제 테스트")
    void deleteRoutineTest() {
        when(routineRepository.findById(any(Long.class))).thenReturn(Optional.of(routine));

        ResponseDeleteRoutineVO result = routineServiceImpl.deleteRoutine(1L);

        assertNotNull(result);
        verify(routineRepository, times(1)).delete(any(Routine.class));
    }

    @Test
    @Transactional
    @DisplayName("운동루틴 삭제 테스트 - 루틴이 없을 때 예외 발생")
    void deleteRoutine_ThrowExceptionTest() {
        when(routineRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(CommonException.class, () -> routineServiceImpl.deleteRoutine(1L));
    }
}
