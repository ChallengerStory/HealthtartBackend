package com.dev5ops.healthtart.user.service;

import com.dev5ops.healthtart.inbody.service.InfraInbodyService;
import com.dev5ops.healthtart.rival.domain.dto.user.RivalUserEntityDTO;
import com.dev5ops.healthtart.user.domain.dto.inbody.UserInbodyDTO;
import com.dev5ops.healthtart.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfraUserService { // 나중에 impl로 고쳐야함.

    private final UserRepository userRepository;
    private final InfraInbodyService infraInbodyService;

    @Autowired
    public InfraUserService(UserRepository userRepository, InfraInbodyService infraInbodyService) {
        this.userRepository = userRepository;
        this.infraInbodyService = infraInbodyService;
    }



    public RivalUserEntityDTO findUserDetailByUserCode(String userCode) {



        UserInbodyDTO inbodyDTO = infraInbodyService.findInbodyDetailByUserCode(userCode);
    }
}
