package com.dev5ops.healthtart.inbody.repository;

import com.dev5ops.healthtart.inbody.aggregate.Inbody;
import com.dev5ops.healthtart.inbody.dto.InbodyUserDTO;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InbodyRepository extends JpaRepository<Inbody, Long> {
    Optional<Inbody> findByDayOfInbodyAndUser(LocalDateTime dayOfInbody, UserEntity user);

    Optional<Inbody> findByInbodyCodeAndUser_UserCode(Long inbodyCode, String userCode);

    List<Inbody> findAllByUser_UserCode(String userCode);

    @Query("SELECT new com.dev5ops.healthtart.inbody.dto.InbodyUserDTO(" +
            "u.userNickname, u.userGender, i.height, i.weight, i.muscleWeight, " +
            "i.fatPercentage, i.basalMetabolicRate, i.inbodyScore) " +
            "FROM inbody i " +
            "JOIN i.user u " +
            "WHERE (u.userCode, i.createdAt) IN (" +
            "    SELECT i2.user.userCode, MAX(i2.createdAt) " +
            "    FROM inbody i2 " +
            "    GROUP BY i2.user.userCode" +
            ") " +
            "ORDER BY i.inbodyScore DESC")
    List<InbodyUserDTO> findLatestInbodyRankings();
}
