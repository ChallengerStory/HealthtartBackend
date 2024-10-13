package com.dev5ops.healthtart.rival.service;


import com.dev5ops.healthtart.rival.domain.dto.RivalUserInbodyDTO;
import com.dev5ops.healthtart.rival.domain.dto.RivalUserInbodyScoreDTO;
import com.dev5ops.healthtart.rival.domain.entity.Rival;
import com.dev5ops.healthtart.rival.repository.RivalRepository;
import com.dev5ops.healthtart.user.domain.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service("rivalService")
public class RivalServiceImpl implements RivalService {

    private final RivalRepository rivalRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RivalServiceImpl(RivalRepository rivalRepository, ModelMapper modelMapper) {
        this.rivalRepository = rivalRepository;
        this.modelMapper = modelMapper;
    }

    // 1. 내 라이벌 조회
    public List<RivalUserInbodyScoreDTO> findRivalById(){
        String userCode = getUserCode();

        // 현재 로그인한 유저가 라이벌로 설정한 사람들 + 그 사람들 정보를 담은 코드 (키, 몸무게, 나이 등등)
        List<RivalUserInbodyScoreDTO> rivalUserInbodyScoreList = rivalRepository.findRivalUsersInbodyScoreByUserCode(userCode);

        return rivalUserInbodyScoreList;
    }

    // 2. 선택한 라이벌 조회 -> 내꺼하고 상대꺼 2개 보여줘야함. -> 결국 유저 정보가 필요하구나? user를 infra로 가져와야한다.
    public List<RivalUserInbodyDTO> findRival(String rivalUserCode){
        // 현재 인증된 사용자 가져오기
        String userCode = getUserCode();

        RivalUserInbodyDTO userInbodyDTO = rivalRepository.findUserInbodyByUserCode(userCode);
        RivalUserInbodyDTO rivalUserInbodyDTO = rivalRepository.findUserInbodyByUserCode(rivalUserCode);
        // 여기서 rivalmatchcode를 어떻게 넘겨줄지? 생각해보기 -> 상세정보를 봤을때에도 삭제할 수 있는게 더 편하지 않을까 싶어서.


        return Arrays.asList(userInbodyDTO, rivalUserInbodyDTO);
    }

    // 3. 라이벌 삭제 -> 라이벌 수정은 필요없을거같음. 라이벌 리스트에서 오른쪽에 삭제 버튼 만들어놓고 그걸 누르면 삭제되게 하는 로직으로 가자.
    // 그리고 삭제를 한다는게 flag를 바꾸는게 아닌거같음. user에만 flag를 만들어놓고 진행? -> 일단 하자.
    public void deleteRival(Long rivalMatchCode){

        Rival rival = rivalRepository.findById(rivalMatchCode)
                .orElseThrow(IllegalArgumentException::new); // 수정해주기

        rivalRepository.delete(rival);
    }





    public String getUserCode(){
        // 현재 인증된 사용자 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 현재 로그인한 유저의 유저코드
        return userDetails.getUserDTO().getUserCode();
    }
}
