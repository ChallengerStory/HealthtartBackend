package com.dev5ops.healthtart.user.service;

import com.dev5ops.healthtart.common.util.JwtUtil;
import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import com.dev5ops.healthtart.user.domain.vo.request.RequestInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseInsertUserVO;
import com.dev5ops.healthtart.user.repository.UserRepository;
import org.apache.catalina.filters.ExpiresFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
    public UserDTO loginUser(String userEmail, String userPassword) {
        // 회원 조회 (이메일로 사용자 조회)
        UserEntity userEntity = userRepository.findUserByUserEmail(userEmail);

        System.out.println(userEntity);
        // 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(userPassword, userEntity.getUserPassword())) {
            throw new BadCredentialsException("잘못된 비밀번호입니다.");
        }

        // 로그인 성공 처리
        // JWT 토큰 발급
        String token = jwtUtil.generateToken(userEmail);

        // 사용자 정보를 DTO로 변환해 반환
        UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
        userDTO.setToken(token);

        return userDTO;
    }

    // 회원 전체 조회
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
    public UserDetails findUserByUsername(String userEmail) throws UsernameNotFoundException {

        // 이메일로 회원 조회
        UserEntity user = userRepository.findUserByUserEmail(userEmail);

        // 권한 목록 설정
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 회원의 UserType에 따라 권한 부여
        switch(user.getUserType()) {
            case MEMBER -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
            case ADMIN -> {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
            }
        }

        // userDetails 객체 생성 및 반환
        return new User(
                user.getUserEmail(),
                user.getUserPassword(),
                grantedAuthorities
        );
    }
}
