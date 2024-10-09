package com.dev5ops.healthtart.routine.service;

import com.dev5ops.healthtart.routine.domain.dto.RoutineDTO;
import com.dev5ops.healthtart.routine.domain.entity.Routine;
import com.dev5ops.healthtart.routine.exception.RoutineNotFoundException;
import com.dev5ops.healthtart.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    // 운동 루틴 전체 조회
    public List<Routine> getRoutines(RoutineDTO routineDTO) {
        if (routineDTO.getRoutineCode() == null || routineDTO.getTitle().isEmpty() ) {throw new RoutineNotFoundException();}
        return routineRepository.findAll();
    }

    // 루틴 시작하기 누르면 운동루틴 등록

    // 루틴 멈추기 누르면 운동루틴 삭제

}
