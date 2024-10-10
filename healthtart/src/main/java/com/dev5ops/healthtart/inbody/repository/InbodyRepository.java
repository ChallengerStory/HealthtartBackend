package com.dev5ops.healthtart.inbody.repository;

import com.dev5ops.healthtart.inbody.aggregate.Inbody;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InbodyRepository extends JpaRepository<Inbody, Long> {
    Optional<Inbody> findByDayOfInbodyAndUser(LocalDateTime dayOfInbody, UserEntity user);

    Optional<Inbody> findByInbodyCodeAndUser_UserCode(Long inbodyCode, String userCode);

    List<Inbody> findAllByUser_UserCode(String userCode);
}
