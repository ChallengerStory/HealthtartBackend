package com.dev5ops.healthtart.rival.repository;

import com.dev5ops.healthtart.rival.domain.dto.RivalUserInbodyDTO;
import com.dev5ops.healthtart.rival.domain.dto.RivalUserInbodyScoreDTO;
import com.dev5ops.healthtart.rival.domain.entity.Rival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RivalRepository extends JpaRepository<Rival, Long> {

    // Rival 엔티티에서 user 필드의 userCode 값을 기준으로 데이터를 찾는다는 의미.
    @Query("SELECT r FROM Rival r JOIN FETCH r.user JOIN FETCH r.rivalUser WHERE r.user.userCode = :userCode")
    List<Rival> findByUser_UserCode(String userCode);

    @Query("SELECT new com.dev5ops.healthtart.rival.domain.dto.RivalUserInbodyScoreDTO(r.rivalMatchCode, ru.userCode, ru.userName, ru.userGender, ru.userHeight, ru.userWeight, ru.userAge, ru.userFlag, i.inbodyScore) " +
            "FROM Rival r " +
            "JOIN r.user u " +
            "JOIN r.rivalUser ru " +
            "JOIN FETCH Inbody i ON i.user.userCode = ru.userCode " +  // Fetch join을 사용하여 즉시 데이터 로드
            "WHERE u.userCode = :userCode " +
            "AND i.createdAt = (SELECT MAX(i2.createdAt) FROM Inbody i2 WHERE i2.user.userCode = ru.userCode)")
    List<RivalUserInbodyScoreDTO> findRivalUsersInbodyScoreByUserCode(@Param("userCode") String userCode);


    @Query("SELECT new com.dev5ops.healthtart.rival.domain.dto.RivalUserInbodyDTO(u.userCode, u.userName, u.userGender, u.userHeight, u.userWeight, u.userAge, u.userFlag, i.inbodyScore, i.height, i.weight, i.muscleWeight, i.fatWeight, i.bmi, i.fatPercentage, i.basalMetabolicRate) " +
            "FROM UserEntity u " +
            "JOIN FETCH Inbody i ON i.user.userCode = u.userCode " +  // Fetch join을 사용하여 즉시 데이터 로드
            "WHERE u.userCode = :userCode " +
            "AND i.createdAt = (SELECT MAX(i2.createdAt) FROM Inbody i2 WHERE i2.user.userCode = u.userCode)")
    RivalUserInbodyDTO findUserInbodyByUserCode(@Param("userCode") String userCode);
}
