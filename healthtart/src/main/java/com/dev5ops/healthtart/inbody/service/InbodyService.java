package com.dev5ops.healthtart.inbody.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.inbody.aggregate.Inbody;
import com.dev5ops.healthtart.inbody.dto.InbodyDTO;
import com.dev5ops.healthtart.inbody.repository.InbodyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service("inbodyService")
public class InbodyService {
    private final InbodyRepository inbodyRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public InbodyDTO registerInbody(InbodyDTO inbodyDTO) {
        Optional<Inbody> existingInbody = inbodyRepository.findByDayOfInbodyAndUser(inbodyDTO.getDayOfInbody(), inbodyDTO.getUser());
        if (existingInbody.isPresent()) {
            throw new CommonException(StatusEnum.DAY_OF_INBODY_DUPLICATE);
        }

        Inbody inbody = modelMapper.map(inbodyDTO, Inbody.class);
        inbody.setCreatedAt(LocalDateTime.now());
        inbody.setUpdatedAt(LocalDateTime.now());

        Inbody savedInbody = inbodyRepository.save(inbody);
        return modelMapper.map(savedInbody, InbodyDTO.class);
    }

}
