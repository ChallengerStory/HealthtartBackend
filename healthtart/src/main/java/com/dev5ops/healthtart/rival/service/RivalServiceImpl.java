package com.dev5ops.healthtart.rival.service;


import com.dev5ops.healthtart.rival.domain.dto.user.RivalUserEntityDTO;
import com.dev5ops.healthtart.rival.domain.dto.RivalDTO;
import com.dev5ops.healthtart.rival.domain.entity.Rival;
import com.dev5ops.healthtart.rival.repository.RivalRepository;
import com.dev5ops.healthtart.user.domain.CustomUserDetails;
import com.dev5ops.healthtart.user.service.InfraUserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RivalServiceImpl implements RivalService {

    private final RivalRepository rivalRepository;
    private final ModelMapper modelMapper;
    private final InfraUserService infraUserService;

    @Autowired
    public RivalServiceImpl(RivalRepository rivalRepository, ModelMapper modelMapper, InfraUserService infraUserService) {
        this.rivalRepository = rivalRepository;
        this.modelMapper = modelMapper;
        this.infraUserService = infraUserService;
    }

    // 1. 내 라이벌 조회
    public List<RivalDTO> findRivalById(){
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String userCode = userDetails.getUserDTO().getUserCode();

        List<Rival> rivalList = rivalRepository.findByUser_UserCode(userCode);

        return rivalList.stream()
                .map(rival -> modelMapper.map(rival, RivalDTO.class))
                .collect(Collectors.toList());
    }

    // 2. 선택한 라이벌 조회 -> 내꺼하고 상대꺼 2개 보여줘야함. -> 결국 유저 정보가 필요하구나? user를 infra로 가져와야한다.
    public List<RivalDTO> findRival(String rivalUserCode){
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String userCode = userDetails.getUserDTO().getUserCode();

        // userCode와 rivalUserCode에 해당하는 user의 정보를 가져와야한다.
        // -> 라이벌에는 어떤 정보가 필요할까? 일단 usercode를 주면 찾아주는 메서드로 만들기
        // 1대1로 비교하는것만 존재할거잖아? 그러면 내꺼 상대꺼 2개 파라미터 줘서 한꺼번에 처리하는게 어떨까?
        // -> 유저이름, 유저 신장, 몸무게, 나이 -> 인바디 정보도 필요할거같은데????? 조졋다 -> 그러면 결국 DTO로 받을껀데 어떤 DTO??
        // RivalCompareDTO로 할까?? 그러자.

        RivalUserEntityDTO userCompareDTO = infraUserService.findUserDetailByUserCode(userCode);
        RivalUserEntityDTO rivalUserCompareDTO = infraUserService.findUserDetailByUserCode(userCode); // 그냥 라이벌에서 유저코드 가지고 인바디 정보 부르면 되지않나? 유저코드 알잖아...
        // 그럼 userinbodydto가 아니라 rivalinbodydto로 하자는 말인가???? 하 어캐해야할지 모르겠네

    }


}
