package com.dev5ops.healthtart.user.service;

import com.dev5ops.healthtart.security.JwtUtil;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import com.dev5ops.healthtart.user.domain.vo.request.RequestInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseInsertUserVO;
import com.dev5ops.healthtart.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, UserRepository userRepository, JwtUtil jwtUtil) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // 회원가입
    @Override
    public ResponseInsertUserVO signUpUser(RequestInsertUserVO request) {

        // Redis에서 이메일 인증 여부 확인
//        String emailVerificationStatus = stringRedisTemplate.opsForValue().get(requestRegisterUserVO.getUserEmail());
        // -> 레디스 이메일 인증 처리는 일반회원만 해야한다.

        String curDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString();

        String userCode = curDate + "-" + uuid;

        request.changePwd(bCryptPasswordEncoder.encode(request.getUserPassword()));

        UserDTO userDTO = UserDTO.builder()
                .userCode(userCode)
                .userType(request.getUserType())
                .userName(request.getUserName())
                .userEmail(request.getUserEmail())
                .userPassword(request.getUserPassword())
                .userPhone(request.getUserPhone())
                .userNickname(request.getUserNickname())
                .userAddress(request.getUserAddress())
                .userFlag(true)
                .userGender("M")
                .userHeight(request.getUserHeight())
                .userWeight(request.getUserWeight())
                .userAge(request.getUserAge())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserEntity insertUser = modelMapper.map(userDTO, UserEntity.class);

        userRepository.save(insertUser);

        return new ResponseInsertUserVO(userDTO);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<UserEntity> allUsers = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();

        for(UserEntity user : allUsers) {
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        UserEntity findUser = userRepository.findByUserEmail(email);

        return modelMapper.map(findUser, UserDTO.class);
    }

    @Override
    public UserDTO findById(String userCode) {
        UserEntity user = userRepository.findById(userCode).get();

        return modelMapper.map(user, UserDTO.class);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
