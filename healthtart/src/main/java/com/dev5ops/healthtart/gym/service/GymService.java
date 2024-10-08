package com.dev5ops.healthtart.gym.service;

import com.dev5ops.healthtart.gym.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service("gymService")
public class GymService {
    private final GymRepository gymRepository;
    private final ModelMapper modelMapper;
}
