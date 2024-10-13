package com.dev5ops.healthtart.rival.controller;

import com.dev5ops.healthtart.rival.domain.dto.RivalDTO;
import com.dev5ops.healthtart.rival.domain.dto.RivalUserInbodyDTO;
import com.dev5ops.healthtart.rival.domain.dto.RivalUserInbodyScoreDTO;
import com.dev5ops.healthtart.rival.service.RivalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rival")
public class RivalController {

    private RivalService rivalService;

    @Autowired
    public RivalController(RivalService rivalService) {
        this.rivalService = rivalService;
    }

    // 1. 모든 라이벌 조회하는 기능
    @GetMapping
    public ResponseEntity<List<RivalUserInbodyScoreDTO>> findRivalList(){
        // VO로 변경해야 한다면 변경하기 DTO <-> VO -> 데이터는 둘이 똑같이 해서 하기. 원칙?
        List<RivalUserInbodyScoreDTO> rivalList = rivalService.findRivalList();

        return ResponseEntity.ok().body(rivalList);
    }

    // 2. 선택한 라이벌 조회하는 기능
    @GetMapping("/{rivalusercode}")
    public ResponseEntity<List<RivalUserInbodyDTO>> findRival(@PathVariable String rivalusercode){

        List<RivalUserInbodyDTO> meAndRivalUserInbodyList = rivalService.findRival(rivalusercode);

        return ResponseEntity.ok().body(meAndRivalUserInbodyList);
    }

    // 3. 선택한 라이벌 삭제
    @DeleteMapping("/{rivalMatchCode}")
    public ResponseEntity<String> deleteRival(@PathVariable Long rivalMatchCode){

        rivalService.deleteRival(rivalMatchCode);

        return ResponseEntity.ok().body("삭제가 완료되었습니다.");
    }

    // 4. 선택한 유저 라이벌 등록
    @PostMapping("/{rivalUserCode}")
    public ResponseEntity insertRival(@PathVariable String rivalUserCode){
        RivalDTO rivalDTO = rivalService.insertRival(rivalUserCode);

        return ResponseEntity.ok().body(rivalDTO);
    }
}
