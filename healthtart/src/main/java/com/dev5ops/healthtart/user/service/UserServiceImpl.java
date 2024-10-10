package com.dev5ops.healthtart.user.service;

import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.domain.entity.User;
import com.dev5ops.healthtart.user.domain.vo.request.RequestInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseInsertUserVO;
import com.dev5ops.healthtart.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 이건 회원가입을 하는 코드. -> 일단 일반 회원부터 처리해보자.
    @Override
    public ResponseInsertUserVO signUpUser(RequestInsertUserVO request) {

        // Redis에서 이메일 인증 여부 확인
//        String emailVerificationStatus = stringRedisTemplate.opsForValue().get(requestRegisterUserVO.getUserEmail());
        // -> 레디스 이메일 인증 처리는 일반회원만 해야한다.

        String curDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString();

        String userCode = curDate + "-" + uuid.substring(0);

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
                .userGender("M")
                .userHeight(request.getUserHeight())
                .userWeight(request.getUserWeight())
                .userAge(request.getUserAge())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(); // gym은 비워놓기

        User insertUser = modelMapper.map(userDTO, User.class);

        userRepository.save(insertUser);


        return new ResponseInsertUserVO(userDTO);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> allUsers = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : allUsers) {
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        User findUser = userRepository.findByUserEmail(email);

        return modelMapper.map(findUser, UserDTO.class);
    }

    @Override
    public UserDTO findById(String userCode) {
        User user = userRepository.findById(userCode).get();

        return modelMapper.map(user, UserDTO.class);
    }


    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User loginUser = userRepository.findByUserEmail(userEmail);

        // 밑에부분 예외처리 수정 필요
        if (loginUser == null) {throw new UsernameNotFoundException(userEmail);}

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        switch (loginUser.getUserType()) {
            case MEMBER -> grantedAuthorities.add(new SimpleGrantedAuthority("MEMBER"));
            case ADMIN -> {
                grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                grantedAuthorities.add(new SimpleGrantedAuthority("MEMBER"));
            }
        }

        // 여기서 사용되는 User는 우리의 User와 다름.
        return new org.springframework.security.core.userdetails.User(loginUser.getUserEmail(), loginUser.getUserPassword(),  true, true, true, true, grantedAuthorities);
    }
}
