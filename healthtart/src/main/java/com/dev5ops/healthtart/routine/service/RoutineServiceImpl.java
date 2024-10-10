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

    // 루틴 시작하기 누르면 운동루틴 등록
    @Override
    @Transactional
    public ResponseInsertRoutineVO registerRoutine(RoutineDTO routineDTO) {
        validateRoutineDTO(routineDTO);

        Routine routine = Routine.builder()
                .routineCode(routineDTO.getRoutineCode())
                .title(routineDTO.getTitle())
                .time(routineDTO.getTime())
                .link(routineDTO.getLink())
                .recommendMusic(routineDTO.getRecommendMusic())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        routineRepository.save(routine);

        return modelMapper.map(routine, ResponseInsertRoutineVO.class);
    }

    // 운동루틴 제목, 할시간 수정 가능
    @Override
    @Transactional
    public ResponseModifyRoutineVO modifyRoutine(Long routineCode, EditRoutineVO modifyRoutine) {
        Routine routine = routineRepository.findById(routineCode)
                .orElseThrow(() -> new CommonException(StatusEnum.ROUTINE_NOT_FOUND));
        routine.toUpdate(modifyRoutine);
        routineRepository.save(routine);
        return modelMapper.map(routine, ResponseModifyRoutineVO.class);
    }

    // 루틴 멈추기 누르면 운동루틴 삭제
    @Override
    @Transactional
    public ResponseDeleteRoutineVO deleteRoutine(Long routineCode) {
        Routine routine = routineRepository.findById(routineCode)
                .orElseThrow(() -> new CommonException(StatusEnum.ROUTINE_NOT_FOUND));
        routineRepository.delete(routine);
        return new ResponseDeleteRoutineVO();
    }

    // DTO 검증 메서드
    private void validateRoutineDTO(RoutineDTO routineDTO) {
        if (routineDTO.getRoutineCode() == null || routineDTO.getTitle().isEmpty()) {
            throw new CommonException(StatusEnum.INVALID_PARAMETER_FORMAT);
        }
    }
}


