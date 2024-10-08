package com.dev5ops.healthtart.user.service;

import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.domain.entity.User;
import com.dev5ops.healthtart.user.domain.vo.request.RequestInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseInsertUserVO;
import com.dev5ops.healthtart.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 이건 회원가입을 하는 코드. -> 일단 일반 회원부터 처리해보자.
    public ResponseInsertUserVO signUpUser(RequestInsertUserVO request) {

        // Redis에서 이메일 인증 여부 확인
//        String emailVerificationStatus = stringRedisTemplate.opsForValue().get(requestRegisterUserVO.getUserEmail());
        // -> 레디스 이메일 인증 처리는 일반회원만 해야한다.

        String curDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString();

        String userCode = curDate + uuid.substring(0);

        request.changePwd(bCryptPasswordEncoder.encode(request.getUserPassword()));

        UserDTO userDTO = UserDTO.builder()
                .userCode(userCode)
                .userType(request.getUserType())
                .userName(request.getUserName())
                .userEmail(request.getUserEmail())
                .userPassword(request.getUserPassword())
                .userPhone(request.getUserPhone())
                .nickname(request.getNickname())
                .userAddress(request.getUserAddress())
                .userFlag(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(); // gym은 비워놓기

        User insertUser = modelMapper.map(userDTO, User.class);

        userRepository.save(insertUser);


        return new ResponseInsertUserVO(userDTO);
    }
}
