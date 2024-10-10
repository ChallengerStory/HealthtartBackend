package com.dev5ops.healthtart.routine.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.routine.domain.dto.RoutineDTO;
import com.dev5ops.healthtart.routine.domain.entity.Routine;
import com.dev5ops.healthtart.routine.domain.vo.*;
import com.dev5ops.healthtart.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {

    private final RoutineRepository routineRepository;
    private final ModelMapper modelMapper;

    // 운동 루틴 전체 조회
    @Override
    public List<ResponseFindRoutineVO> getRoutines() {
        List<Routine> routinesList = routineRepository.findAll();
        if (routinesList.isEmpty()) throw new CommonException(StatusEnum.ROUTINE_NOT_FOUND);
        return routinesList.stream()
                .map(routine -> modelMapper.map(routine, ResponseFindRoutineVO.class))
                .collect(Collectors.toList());
    }

    // 운동 루틴 단일 조회
    @Override
    public ResponseFindRoutineVO findRoutineByCode(Long routineCode) {
        Routine routine = routineRepository.findById(routineCode)
                .orElseThrow(() -> new CommonException(StatusEnum.ROUTINE_NOT_FOUND));
        return modelMapper.map(routine, ResponseFindRoutineVO.class);
    }


}


