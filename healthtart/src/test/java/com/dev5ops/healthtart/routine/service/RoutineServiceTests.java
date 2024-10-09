package com.dev5ops.healthtart.routine.service;

import com.dev5ops.healthtart.routine.domain.dto.RoutineDTO;
import com.dev5ops.healthtart.routine.domain.entity.Routine;
import com.dev5ops.healthtart.routine.repository.RoutineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RoutineServiceTests {

    @Mock
    private RoutineRepository routineRepository;

    @InjectMocks
    private RoutineService routineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoutines_Success() {

        Routine routine1 = new Routine(1L, "김정은도 10kg 감량한 모닝 루틴",
                null, "healthtart.com", "Dynamite - 방탄소년단 (BTS)",
                LocalDateTime.now(), LocalDateTime.now());

        Routine routine2 = new Routine(2L, "운동 루틴 2",
                null, "healthtart.com", "Lovesick Girls - 블랙핑크",
                LocalDateTime.now(), LocalDateTime.now());

        when(routineRepository.findAll()).thenReturn(Arrays.asList(routine1, routine2));
        RoutineDTO routineDTO = new RoutineDTO();
        routineDTO.setRoutineCode(1L);
        routineDTO.setTitle("김정은도 10kg 감량한 모닝 루틴");

        List<Routine> result = routineService.getRoutines(routineDTO);
        assertEquals(2, result.size());
        assertEquals("김정은도 10kg 감량한 모닝 루틴", result.get(0).getTitle());
    }
}
