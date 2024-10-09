package com.dev5ops.healthtart.user.service;


import com.dev5ops.healthtart.user.domain.UserTypeEnum;
import com.dev5ops.healthtart.user.domain.entity.User;
import com.dev5ops.healthtart.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

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
            providerId = String.valueOf(attributes.get("id"));
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
            email = (String) kakaoAccount.get("email");
            name = (String) kakaoProfile.get("nickname");
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
        }

        // provider는 그거고 (kakao인지 google인지)
        // providerId는 식별자임 (code)
        User user = userRepository.findByProviderAndProviderId(provider, providerId);

        if (user == null) {
            // 새 사용자 생성
            user = User.builder()
                    .userCode(UUID.randomUUID().toString())
                    .userType(UserTypeEnum.MEMBER)
                    .userName(name)
                    .userEmail(email)
                    .userPassword(null)  // OAuth2 사용자는 비밀번호가 없음
                    .userPhone("")  // 필요한 경우 추가 정보를 받아야 함
                    .nickname(name)  // 닉네임을 이름으로 초기 설정
                    .userAddress("")  // 필요한 경우 추가 정보를 받아야 함
                    .userFlag(true)  // 활성 사용자로 설정
                    .provider(provider)
                    .providerId(providerId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else {
            // 기존 사용자 정보 업데이트
            user.setUserName(name);
            user.setUserEmail(email);
            user.setUpdatedAt(LocalDateTime.now());
        }

        user = userRepository.save(user);

        // User 정보를 포함한 attributes 맵 생성
        Map<String, Object> userAttributes = new HashMap<>(attributes);
        userAttributes.put("user", user);

        // 제공자별 ID를 주요 식별자로 사용
        String nameAttributeKey = "google".equals(provider) ? "sub" : "id";

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getUserType().name())),
                userAttributes,
                nameAttributeKey
        );
    }
}