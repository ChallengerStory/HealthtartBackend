package com.dev5ops.healthtart.gym.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.gym.aggregate.Gym;
import com.dev5ops.healthtart.gym.aggregate.vo.request.RequestRegisterGymVO;
import com.dev5ops.healthtart.gym.aggregate.vo.response.ResponseRegisterGymVO;
import com.dev5ops.healthtart.gym.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service("gymService")
public class GymService {
    private final GymRepository gymRepository;
    private final ModelMapper modelMapper;

    public ResponseRegisterGymVO registerGym(RequestRegisterGymVO requestRegisterGymVO) {
        Gym gym = Gym.builder()
                .gymName(requestRegisterGymVO.getGymName())
                .address(requestRegisterGymVO.getAddress())
                .businessNumber(requestRegisterGymVO.getBusinessNumber())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if (gymRepository.findByBusinessNumber(gym.getBusinessNumber()).isPresent()) throw new CommonException(StatusEnum.GYM_DUPLICATE);

        gymRepository.save(gym);
        return modelMapper.map(gym, ResponseRegisterGymVO.class);
    }


}
