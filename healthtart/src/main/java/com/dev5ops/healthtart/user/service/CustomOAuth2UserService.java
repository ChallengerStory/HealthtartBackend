package com.dev5ops.healthtart.user.service;


import com.dev5ops.healthtart.user.domain.UserTypeEnum;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import com.dev5ops.healthtart.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser1");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("loadUser2");

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String providerId;
        String email;
        String name;

        if ("google".equals(provider)) {
            providerId = (String) attributes.get("sub");
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        } else if ("kakao".equals(provider)) {
            // 카카오 고유 ID 가져오기
            providerId = String.valueOf(attributes.get("id"));

            // 카카오 계정 정보에서 프로필 정보 가져오기
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

            // 이메일은 요청하지 않으므로 주석 처리 또는 제거
            // email = (String) kakaoAccount.get("email");
            email = "kakao@gmail.com입니당";
            // 이름(닉네임)만 가져오기
            name = (String) kakaoProfile.get("nickname");
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
        }

        // provider는 그거고 (kakao인지 google인지)
        // providerId는 식별자임 (code)
        UserEntity user = userRepository.findByProviderAndProviderId(provider, providerId);


        if (user == null) {

            // 새 사용자 생성
            String curDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String uuid = UUID.randomUUID().toString();

            String userCode = curDate + "-" + uuid.substring(0);

            user = UserEntity.builder()
                    .userCode(userCode)
                    .userType(UserTypeEnum.MEMBER)
                    .userName(name)
                    .userEmail(email)
                    .userPassword(null)  // OAuth2 사용자는 비밀번호가 없음
                    .userPhone("빵빵아")  // 필요한 경우 추가 정보를 받아야 함
                    .userNickname(name)  // 닉네임을 이름으로 초기 설정
                    .userAddress("옥지얌")  // 필요한 경우 추가 정보를 받아야 함
                    .userFlag(true)  // 활성 사용자로 설정
                    .provider(provider)
                    .providerId(providerId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } /*else {
            // 기존 사용자 정보 업데이트
            user.setUserName(name);
            user.setUserEmail(email);
            user.setUpdatedAt(LocalDateTime.now());
        }*/

        user = userRepository.save(user);
        // 여기 위에까지가 회원가입 하는것.(또는 수정)


        log.info("customService user : {} ", user.toString());
        // User 정보를 포함한 attributes 맵 생성
        Map<String, Object> userAttributes = new HashMap<>(attributes);
        userAttributes.put("user", user);

        // 제공자별 ID를 주요 식별자로 사용 -> 카카오는 "id"
        String nameAttributeKey = "google".equals(provider) ? "sub" : "id";

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getUserType().name())),
                userAttributes,
                nameAttributeKey
        );
    }
}