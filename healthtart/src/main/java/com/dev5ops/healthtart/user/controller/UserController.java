package com.dev5ops.healthtart.user.controller;

import com.dev5ops.healthtart.security.JwtUtil;
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

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    // Environment는 왜 쓰는거지???
    private JwtUtil jwtUtil;
    private Environment env;
    private ModelMapper modelMapper;
    private UserService userService;

    @Autowired
    public UserController(JwtUtil jwtUtil, ModelMapper modelMapper, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<ResponseInsertUserVO> insertUser(@RequestBody RequestInsertUserVO request) {
        ResponseInsertUserVO responseUser = userService.signUpUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
}