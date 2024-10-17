package com.dev5ops.healthtart.user.controller;

import com.dev5ops.healthtart.user.domain.dto.ResponseMypageDTO;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.security.JwtUtil;
import com.dev5ops.healthtart.user.domain.vo.request.RequestInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.request.RequestOauth2VO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseFindUserVO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseMypageVO;
import com.dev5ops.healthtart.user.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dev5ops.healthtart.user.domain.UserTypeEnum.MEMBER;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    private JwtUtil jwtUtil;
    private Environment env;
    private ModelMapper modelMapper;
    private UserService userService;

    @Autowired
    public UserController(JwtUtil jwtUtil, Environment env, ModelMapper modelMapper, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.env = env;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseInsertUserVO> insertUser(@RequestBody RequestInsertUserVO request) {
        if (request.getUserType() == null) {
            request.setUserType("MEMBER");
        }
        ResponseInsertUserVO responseUser = userService.signUpUser(request);
        log.info("여기 오긴 왔나요??");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/mypage")
    public ResponseEntity<ResponseMypageVO> getMypageInfo(){

        ResponseMypageDTO mypageInfo = userService.getMypageInfo();

        ResponseMypageVO response = modelMapper.map(mypageInfo, ResponseMypageVO.class);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 회원 전체 조회
    @GetMapping
    public ResponseEntity<List<ResponseFindUserVO>> getAllUsers() {
        // service에서 DTO 형태로 찾은 애를 VO로 바꿔야한다
        List<UserDTO> userDTOList = userService.findAllUsers();
        List<ResponseFindUserVO> userVOList = userDTOList.stream()
                .map(userDTO -> modelMapper.map(userDTO, ResponseFindUserVO.class))
                .collect(Collectors.toList());

                return new ResponseEntity<>(userVOList, HttpStatus.OK);
    }

    // 이메일로 회원 정보 조회
    @GetMapping("/email/{email}")
    public ResponseEntity<ResponseFindUserVO> findUserByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.findUserByEmail(email);
        ResponseFindUserVO responseFindUserVO = modelMapper.map(userDTO, ResponseFindUserVO.class);

        return new ResponseEntity<>(responseFindUserVO, HttpStatus.OK);
    }

    // 회원 코드로 회원 정보 조회
    @GetMapping("/usercode/{userCode}")
    public ResponseEntity<ResponseFindUserVO> findUserById(@PathVariable String userCode) {
        UserDTO userDTO = userService.findById(userCode);
        ResponseFindUserVO responseFindUserVO = modelMapper.map(userDTO, ResponseFindUserVO.class);

        return new ResponseEntity<>(responseFindUserVO, HttpStatus.OK);
    }

    // 회원 탈퇴
    @PatchMapping("/delete/{userCode}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userCode) {
        userService.deleteUser(userCode);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/nickname/check") // users/nickname/check
    public ResponseEntity<Map<String, Boolean>> checkDuplicateNickname(String userNickname){

        Boolean isDuplicate = userService.checkDuplicateNickname(userNickname);

        // JSON 형태로 반환할 Map 생성
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);

        return ResponseEntity.status(HttpStatus.OK).body(response); // true이면 사용 불가
    }

    // 여기서 회원가입 시킬 예정
    @PostMapping("/oauth2")
    public ResponseEntity<String> saveOauth2User(@RequestBody RequestOauth2VO requestOauth2VO){

        // userCode는 여기서 생성해서 저장하자.
        // member type도 여기서
        // flag도 여기서

        userService.saveOauth2User(requestOauth2VO);

        return ResponseEntity.status(HttpStatus.OK).body("잘 저장했습니다");
    }

}
