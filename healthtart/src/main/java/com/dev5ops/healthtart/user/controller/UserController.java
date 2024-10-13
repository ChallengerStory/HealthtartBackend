package com.dev5ops.healthtart.user.controller;

import com.dev5ops.healthtart.user.domain.CustomUserDetails;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.security.JwtUtil;
import com.dev5ops.healthtart.user.domain.vo.request.RequestInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseFindUserVO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseInsertUserVO;
import com.dev5ops.healthtart.user.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
import com.google.api.Http;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        ResponseInsertUserVO responseUser = userService.signUpUser(request);
        log.info("여기 오긴 왔나요??");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/login/google")
//    public ResponseEntity<?> handleGoogleLogin(@RequestHeader("Authorization") String authHeader) {
    public ResponseEntity<?> handleGoogleLogin(@RequestParam String token, Authentication authentication) {
        try {
            // "Bearer " 접두사 제거
//            String token = authHeader.substring(7);

            // 토큰 유효성 검사
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            // 토큰에서 이메일 추출
            String email = jwtUtil.getEmailFromToken(token);
            String userCode = jwtUtil.getUserCodeFromToken(token);
            log.info("handle google controller code: {}", userCode);

//            log.info("구글로 로그인 했을때에는 말이야?: {}", authentication.getPrincipal());
            // 사용자 정보 조회
            UserDTO userDTO = userService.findById(userCode);
            UserDTO userDTO2 = userService.findUserByEmail(email);

            log.info("findbyUserCode: {}, findByUserEmail: {}", userDTO, userDTO2);


            if (userDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // 로그인 성공 응답
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Google login successful");
            response.put("user", userDTO);

            return ResponseEntity.ok(response);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }

    @GetMapping("/login/kakao")
//    public ResponseEntity<?> handleGoogleLogin(@RequestHeader("Authorization") String authHeader) {
    public ResponseEntity<?> handleKakaoLogin(@RequestParam String token, Authentication authentication) {
        try {
//            log.info("authentication이 없어서 오류?? : {}", authentication.getPrincipal());
            log.info("authentication 자체가 널인가??: {}", authentication);
            // "Bearer " 접두사 제거
//            String token = authHeader.substring(7);

            // 토큰 유효성 검사 -> 유효성검사가 필요없음. -> 이미 jwtfilter를 거쳐서 나오기 때문
//            if (!jwtUtil.validateToken(token)) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
//            }
            log.info("jwt 토큰 값은: {} ", token);
            // 토큰에서 이메일 추출
//            String email = jwtUtil.getEmailFromToken(token);
            String userCode = jwtUtil.getUserCodeFromToken(token);
            log.info("handle kakao controller code: {}", userCode);
//            log.info("카카오로 로그인 했을때에는 말이야?: {}", authentication.getPrincipal());

            // 사용자 정보 조회
            UserDTO userDTO = userService.findById(userCode);
//            UserDTO userDTO2 = userService.findUserByEmail(email);

//            log.info("findbyUserCode: {}, findByUserEmail: {}", userDTO, userDTO2);


            if (userDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // 로그인 성공 응답
            Map<String, Object> response = new HashMap<>();
            response.put("message", "kakao login successful");
            response.put("user", userDTO);

            return ResponseEntity.ok(response);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> getMyPage(Authentication authentication) {
        log.info("Mypage request received");
        log.info("mypage 시에 모든 authentication 정보 보기: {}", authentication.toString());
            log.info(authentication.toString());

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userCode = userDetails.getUserDTO().getUserCode();  // CustomUserDetails에서 UserDTO로 접근하여 userCode 가져옴
        log.info("userDetails: {}", userDetails.getUserDTO().toString());
        log.info("userCode: {}", userCode);
//            String userEmail = jwtUtil.getEmailFromToken(authentication.getName());
//            log.info("Fetching mypage for user: {}", userEmail);

//            UserDTO userDTO = userService.findUserByEmail(userEmail);
            UserDTO userDTO = userService.findById(userCode);
            if (userDTO == null) {
                log.warn("User not found for userCode: {}", userCode);
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("userCode", userDTO.getUserCode());
            response.put("userName", userDTO.getUserName());
            response.put("userEmail", userDTO.getUserEmail());
            response.put("userType", userDTO.getUserType());
            response.put("nickname", userDTO.getUserNickname());
            // 필요한 추가 정보들...

            log.info("Mypage data successfully retrieved for user: {}", userCode);
            return ResponseEntity.ok(response);
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
    @GetMapping
    public ResponseEntity<ResponseFindUserVO> findUserByEmail(@RequestParam String email) {
        UserDTO userDTO = userService.findUserByEmail(email);
        ResponseFindUserVO responseFindUserVO = modelMapper.map(userDTO, ResponseFindUserVO.class);

        return new ResponseEntity<>(responseFindUserVO, HttpStatus.OK);
    }

    // 회원 코드로 회원 정보 조회
    @GetMapping
    public ResponseEntity<ResponseFindUserVO> findUserById(@RequestParam String userCode) {
        UserDTO userDTO = userService.findById(userCode);
        ResponseFindUserVO responseFindUserVO = modelMapper.map(userDTO, ResponseFindUserVO.class);

        return new ResponseEntity<>(responseFindUserVO, HttpStatus.OK);
    }

}
