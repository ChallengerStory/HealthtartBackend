package com.dev5ops.healthtart.user.repository;

import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    // Optional: null값 담을 수 있는 컨테이너 객체
    // 사용자 존재하지 않을 경우 빈 Optional로 반환
    UserEntity findByUserEmail(String userEmail);

    UserEntity findByProviderAndProviderId(String provider, String providerId);

    UserEntity findByUserNickname(String userNickname);
}
