package com.dev5ops.healthtart.user.controller;

//import com.dev5ops.healthtart.secutiry.JwtUtil;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.domain.vo.request.RequestInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseInsertUserVO;
import com.dev5ops.healthtart.user.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

//    private JwtUtil jwtUtil;
//    private Environment env;
    private ModelMapper modelMapper;
    private UserService userService;

    @Autowired
    public UserController(/*JwtUtil jwtUtil,*//* Environment env,*/ ModelMapper modelMapper, UserService userService) {
//        this.jwtUtil = jwtUtil;
//        this.env = env;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllUsers () {
        List<UserDTO> allUsers = userService.findAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    @PostMapping // /users 와 POST 요청은 회원가입 (필요한 데이터가 뭐가 있을까?) -> RequestInsertUserVO
    public ResponseEntity<ResponseInsertUserVO> insertUser(@RequestBody RequestInsertUserVO request) {
        ResponseInsertUserVO responseUser = userService.signUpUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

}
