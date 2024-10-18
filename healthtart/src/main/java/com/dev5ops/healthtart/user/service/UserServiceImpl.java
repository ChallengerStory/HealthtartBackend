package com.dev5ops.healthtart.user.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.security.JwtUtil;
import com.dev5ops.healthtart.user.domain.CustomUserDetails;
import com.dev5ops.healthtart.user.domain.dto.*;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import com.dev5ops.healthtart.user.domain.vo.request.RequestInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.request.RequestOauth2VO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseEditMypageVO;
import com.dev5ops.healthtart.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;  // StringRedisTemplate 사용
    private final RestTemplate restTemplate;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, UserRepository userRepository, JwtUtil jwtUtil, StringRedisTemplate stringRedisTemplate, RestTemplate restTemplate) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.stringRedisTemplate = stringRedisTemplate;
        this.restTemplate = restTemplate;
    }

    // 회원가입
    @Override
    public ResponseInsertUserDTO signUpUser(RequestInsertUserVO request) {

        // Redis에서 이메일 인증 여부 확인
        String emailVerificationStatus = stringRedisTemplate.opsForValue().get(request.getUserEmail());

        if (!"True".equals(emailVerificationStatus)) {
            log.error("이메일 인증이 완료되지 않았습니다: {}", request.getUserEmail());
            throw new CommonException(StatusEnum.EMAIL_VERIFICATION_REQUIRED); // 이메일 인증이 필요하다는 커스텀 예외 던지기
        }

        if (userRepository.findByUserEmail(request.getUserEmail()) != null) throw new CommonException(StatusEnum.EMAIL_DUPLICATE);

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

        ResponseInsertUserDTO insertUserDTO= modelMapper.map(insertUser, ResponseInsertUserDTO.class);

        return insertUserDTO;
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

    // 이메일로 회원 조회
    @Override
    public UserDTO findUserByEmail(String userEmail) {
        UserEntity findUser = userRepository.findByUserEmail(userEmail);
        if(findUser == null) {
            return null;
        }
        return modelMapper.map(findUser, UserDTO.class);
    }

    @Override
    public UserDTO findById(String userCode) {
        UserEntity user = userRepository.findById(userCode).get();

        return modelMapper.map(user, UserDTO.class);
    }


    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserEmail(userEmail);

        log.info("여기");

        // 사용자가 없을 경우 예외 발생
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + userEmail);
        }

        // 사용자의 권한 설정
        // ADMIN인 경우 MEMBER 속성도 가짐.
        List<GrantedAuthority> roles = new ArrayList<>();
        if("MEMBER".equals(user.getUserType().name())){
            roles.add(new SimpleGrantedAuthority("MEMBER"));
        }else{
            roles.add(new SimpleGrantedAuthority("MEMBER"));
            roles.add(new SimpleGrantedAuthority("ADMIN"));
        }

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if(userDTO.getUserFlag() == false) throw new UsernameNotFoundException("User not found with email: " + userEmail);
        return new CustomUserDetails(
                                    userDTO,
                                    roles,
                                    true,
                                    true,
                                    true,
                                    user.getUserFlag());
    }

    @Override
    public void deleteUser(String userCode) {
        UserEntity user = userRepository.findById(userCode).orElseThrow(() -> new CommonException(StatusEnum.USER_NOT_FOUND));

        user.removeRequest(user);
        userRepository.save(user);
    }

    @Override
    public Boolean checkDuplicateNickname(String userNickname) {

        Boolean response = true;

        // 특수기호 확인을 위한 정규표현식
        String specialCharacters = "[!@#$%^&*()_+=|<>?{}\\[\\]~-]";
        // 특수기호가 포함되어 있는지 확인
        if (userNickname.matches(".*" + specialCharacters + ".*")) return response;

        UserEntity user = userRepository.findByUserNickname(userNickname);
        log.info(String.valueOf(response));
        if(user == null){
            response = false;
            log.info(String.valueOf(response));
        }
        log.info(String.valueOf(response));

        return response;
    }

    @Override
    public void saveOauth2User(RequestOauth2VO request) {

        String curDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString();

        String userCode = curDate + "-" + uuid.substring(0);

        UserEntity oauth2User = UserEntity.builder()
                .userCode(userCode)
                .userName(request.getUserName())
                .userEmail(request.getUserEmail())
                .userPhone(request.getUserPhone())
                .userNickname(request.getUserNickname())
                .userAddress(request.getUserAddress())
                .userFlag(true)
                .userGender(request.getUserGender())
                .userHeight(request.getUserHeight())
                .userWeight(request.getUserWeight())
                .userAge(request.getUserAge())
                .provider(request.getProvider())
                .providerId(request.getProviderId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(oauth2User);
    }

    @Override
    public ResponseMypageDTO getMypageInfo() {

        String userCode = getUserCode();

        ResponseMypageDTO responseMypageDTO = userRepository.findMypageInfo(userCode);
        if(responseMypageDTO == null) throw new CommonException(StatusEnum.USER_NOT_FOUND);

        return responseMypageDTO;
    }

    @Override
    public void editMypageInfo(EditMypageDTO editUserDTO) {

        String userCode = getUserCode();
        UserEntity user = userRepository.findById(userCode).orElseThrow(()
                -> new CommonException(StatusEnum.USER_NOT_FOUND));
        user.setUserName(editUserDTO.getUserName());
        user.setUserEmail(editUserDTO.getUserEmail());
        user.setUserPassword(editUserDTO.getUserPassword());
        user.setUserPhone(editUserDTO.getUserPhone());
        user.setUserNickname(editUserDTO.getUserNickname());
        user.setUserGender(editUserDTO.getUserGender());
        user.setUserHeight(editUserDTO.getUserHeight());
        user.setUserWeight(editUserDTO.getUserWeight());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }



    public String getUserCode() {
        // 현재 인증된 사용자 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 인증된 사용자가 문자열(String)인 경우 (로그인하지 않은 상태)
        if (principal instanceof String) {
            throw new CommonException(StatusEnum.USER_NOT_FOUND);
        }

        // 인증된 사용자가 CustomUserDetails인 경우
        CustomUserDetails userDetails = (CustomUserDetails) principal;

        log.info("Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
        log.info("userDetails: {}", userDetails.toString());

        // 현재 로그인한 유저의 유저코드 반환
        return userDetails.getUserDTO().getUserCode();
    }
}