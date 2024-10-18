package com.dev5ops.healthtart.inbody.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.inbody.aggregate.Inbody;
import com.dev5ops.healthtart.inbody.aggregate.vo.request.RequestEditInbodyVO;
import com.dev5ops.healthtart.inbody.dto.InbodyDTO;
import com.dev5ops.healthtart.inbody.dto.InbodyUserDTO;
import com.dev5ops.healthtart.inbody.repository.InbodyRepository;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import com.dev5ops.healthtart.user.repository.UserRepository;
import com.dev5ops.healthtart.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service("inbodyService")
public class InbodyService {
    private final InbodyRepository inbodyRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Transactional
    public InbodyDTO registerInbody(InbodyDTO inbodyDTO) {
        log.info("인바디 등록 중: {}", inbodyDTO);
        inbodyDTO.setUserCode(inbodyDTO.getUser().getUserCode());

        UserDTO userDTO = userService.findById(inbodyDTO.getUserCode());
        if (userDTO == null) {
            log.warn("존재하지 않는 사용자: {}", inbodyDTO.getUserCode());
            throw new CommonException(StatusEnum.USER_NOT_FOUND);
        }
        log.info(userDTO.toString());

        UserEntity user = convertToUserEntity(userDTO);

        Inbody inbody = new Inbody();
        inbody.setInbodyScore(inbodyDTO.getInbodyScore());
        inbody.setWeight(inbodyDTO.getWeight());
        inbody.setHeight(inbodyDTO.getHeight());
        inbody.setMuscleWeight(inbodyDTO.getMuscleWeight());
        inbody.setFatWeight(inbodyDTO.getFatWeight());
        inbody.setBmi(inbodyDTO.getBmi());
        inbody.setFatPercentage(inbodyDTO.getFatPercentage());
        inbody.setDayOfInbody(inbodyDTO.getDayOfInbody());
        inbody.setBasalMetabolicRate(inbodyDTO.getBasalMetabolicRate());
        inbody.setUser(user);
        inbody.setCreatedAt(LocalDateTime.now());
        inbody.setUpdatedAt(LocalDateTime.now());

        Optional<Inbody> existingInbody = inbodyRepository.findByDayOfInbodyAndUser(inbodyDTO.getDayOfInbody(), user);
        if (existingInbody.isPresent()) {
            log.warn("중복 인바디 항목 발견: 날짜: {} 사용자: {}", inbodyDTO.getDayOfInbody(), user);
            throw new CommonException(StatusEnum.DAY_OF_INBODY_DUPLICATE);
        }

        log.info("데이터베이스에 인바디 저장 중: {}", inbody);
        Inbody savedInbody = inbodyRepository.save(inbody);

        log.info("저장된 인바디 객체: {}", savedInbody);
        return modelMapper.map(savedInbody, InbodyDTO.class);
    }


    @Transactional
    public InbodyDTO editInbody(Long inbodyCode, RequestEditInbodyVO request) {
        Inbody inbody = inbodyRepository.findById(inbodyCode).orElseThrow(() -> new CommonException(StatusEnum.INBODY_NOT_FOUND));

        inbody.setInbodyScore(request.getInbodyScore());
        inbody.setWeight(request.getWeight());
        inbody.setHeight(request.getHeight());
        inbody.setMuscleWeight(request.getMuscleWeight());
        inbody.setFatWeight(request.getFatWeight());
        inbody.setBmi(request.getBmi());
        inbody.setFatPercentage(request.getFatPercentage());
        inbody.setDayOfInbody(request.getDayOfInbody());
        inbody.setBasalMetabolicRate(request.getBasalMetabolicRate());
        inbody.setUpdatedAt(LocalDateTime.now());
        inbody.setUser(request.getUser());

        inbody = inbodyRepository.save(inbody);

        return modelMapper.map(inbody, InbodyDTO.class);
    }

    public void deleteInbody(Long inbodyCode) {
        Inbody inbody = inbodyRepository.findById(inbodyCode).orElseThrow(() -> new CommonException(StatusEnum.INBODY_NOT_FOUND));

        inbodyRepository.delete(inbody);
    }

    public InbodyDTO findInbodyByCode(Long inbodyCode) {
        Inbody inbody = inbodyRepository.findById(inbodyCode).orElseThrow(() -> new CommonException(StatusEnum.INBODY_NOT_FOUND));

        return modelMapper.map(inbody, InbodyDTO.class);
    }

    public List<InbodyDTO> findAllInbody() {
        List<Inbody> inbodyList = inbodyRepository.findAll();

        return inbodyList.stream()
                .map(inbody -> modelMapper.map(inbody, InbodyDTO.class))
                .collect(Collectors.toList());
    }

    public InbodyDTO findInbodyByCodeAndUser(Long inbodyCode, String userCode) {
        Inbody inbody = inbodyRepository.findByInbodyCodeAndUser_UserCode(inbodyCode, userCode)
                .orElseThrow(() -> new CommonException(StatusEnum.INBODY_NOT_FOUND));

        return modelMapper.map(inbody, InbodyDTO.class);
    }

    public List<InbodyDTO> findAllInbodyByUser(String userCode) {
        List<InbodyUserDTO> inbodyList = inbodyRepository.findLatestInbodyRankings();
        return inbodyList.stream()
                .map(inbody -> modelMapper.map(inbody, InbodyDTO.class))
                .collect(Collectors.toList());
    }


    public List<InbodyUserDTO> findInbodyUserInbody() {
        List<InbodyUserDTO> dtoList = inbodyRepository.findLatestInbodyRankings();
        log.info("dtoList:{}", dtoList.toString());
        return dtoList;
    }

    public UserEntity convertToUserEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }
}
