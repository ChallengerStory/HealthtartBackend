package com.dev5ops.healthtart.routine.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.routine.domain.dto.RoutineDTO;
import com.dev5ops.healthtart.routine.domain.entity.Routine;
import com.dev5ops.healthtart.routine.domain.vo.*;
import com.dev5ops.healthtart.routine.domain.vo.response.ResponseDeleteRoutineVO;
import com.dev5ops.healthtart.routine.domain.vo.response.ResponseFindRoutineVO;
import com.dev5ops.healthtart.routine.domain.vo.response.ResponseInsertRoutineVO;
import com.dev5ops.healthtart.routine.domain.vo.response.ResponseModifyRoutineVO;
import com.dev5ops.healthtart.routine.repository.RoutineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoutineServiceImplTests {

    @InjectMocks
    private RoutineServiceImpl routineService;

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("루틴이 존재할 때 루틴 목록 조회 테스트")
    void getRoutinesSuccess() {
        Routine routine = new Routine();
        when(routineRepository.findAll()).thenReturn(List.of(routine));
        when(modelMapper.map(any(Routine.class), eq(ResponseFindRoutineVO.class)))
                .thenReturn(new ResponseFindRoutineVO());

        List<ResponseFindRoutineVO> result = routineService.getRoutines();

        assertFalse(result.isEmpty());
        verify(routineRepository).findAll();
    }

    @Test
    @DisplayName("루틴이 없을 때 예외 발생 테스트")
    void getRoutinesFail() {
        when(routineRepository.findAll()).thenReturn(List.of());

        CommonException exception = assertThrows(CommonException.class,
                () -> routineService.getRoutines());
        assertEquals(StatusEnum.ROUTINE_NOT_FOUND, exception.getStatusEnum());
    }

    @Test
    @DisplayName("루틴 코드로 루틴 조회 테스트")
    void findRoutineSuccess() {
        Routine routine = new Routine();
        when(routineRepository.findById(anyLong())).thenReturn(Optional.of(routine));
        when(modelMapper.map(any(Routine.class), eq(ResponseFindRoutineVO.class)))
                .thenReturn(new ResponseFindRoutineVO());

        ResponseFindRoutineVO result = routineService.findRoutineByCode(1L);

        assertNotNull(result);
        verify(routineRepository).findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 루틴을 조회하면 예외 발생 테스트")
    void findRoutineFail() {
        when(routineRepository.findById(anyLong())).thenReturn(Optional.empty());

        CommonException exception = assertThrows(CommonException.class,
                () -> routineService.findRoutineByCode(1L));
        assertEquals(StatusEnum.ROUTINE_NOT_FOUND, exception.getStatusEnum());
    }

    @Test
    @Transactional
    @DisplayName("루틴 등록 테스트")
    void registerRoutineSuccess() {
        RoutineDTO routineDTO = new RoutineDTO();
        routineDTO.setRoutineCode(1L);
        routineDTO.setTitle("김정은도 10kg 감량한 모닝 루틴 !!!");
        routineDTO.setTime(60);
        routineDTO.setLink("http://healthtart.com");
        routineDTO.setRecommendMusic("삐딱하게 - G-DRAGON");

        Routine routine = Routine.builder()
                .routineCode(routineDTO.getRoutineCode())
                .title(routineDTO.getTitle())
                .time(routineDTO.getTime())
                .link(routineDTO.getLink())
                .recommendMusic(routineDTO.getRecommendMusic())
                .build();

        when(routineRepository.save(any(Routine.class))).thenReturn(routine);

        ResponseInsertRoutineVO responseVO = new ResponseInsertRoutineVO();
        when(modelMapper.map(any(Routine.class), eq(ResponseInsertRoutineVO.class)))
                .thenReturn(responseVO);

        ResponseInsertRoutineVO result = routineService.registerRoutine(routineDTO);

        assertNotNull(result);
        verify(routineRepository).save(any(Routine.class));
        verify(modelMapper).map(any(Routine.class), eq(ResponseInsertRoutineVO.class));
    }

    @Test
    @Transactional
    @DisplayName("루틴 수정 테스트")
    void modifyRoutineSuccess() {
        Long routineCode = 1L;

        EditRoutineVO modifyRoutine = new EditRoutineVO("김정은도 10kg 감량한 저녁 루틴 !!!", 90);

        Routine routine = new Routine();
        when(routineRepository.findById(routineCode)).thenReturn(Optional.of(routine));

        ResponseModifyRoutineVO responseVO = new ResponseModifyRoutineVO();
        when(modelMapper.map(any(Routine.class), eq(ResponseModifyRoutineVO.class)))
                .thenReturn(responseVO);

        ResponseModifyRoutineVO result = routineService.modifyRoutine(routineCode, modifyRoutine);

        assertNotNull(result);
        verify(routineRepository).findById(routineCode);
        verify(routineRepository).save(any(Routine.class));
        verify(modelMapper).map(any(Routine.class), eq(ResponseModifyRoutineVO.class));
    }


    @Test
    @Transactional
    @DisplayName("루틴 삭제 테스트")
    void deleteRoutineSuccess() {
        Long routineCode = 1L;
        Routine routine = new Routine();
        when(routineRepository.findById(routineCode)).thenReturn(Optional.of(routine));

        ResponseDeleteRoutineVO result = routineService.deleteRoutine(routineCode);

        assertNotNull(result);
        verify(routineRepository).findById(routineCode);
        verify(routineRepository).delete(routine);
    }


}
