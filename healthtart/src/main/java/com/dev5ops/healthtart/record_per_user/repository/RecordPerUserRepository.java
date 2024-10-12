package com.dev5ops.healthtart.record_per_user.repository;

import com.dev5ops.healthtart.record_per_user.aggregate.RecordPerUser;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordPerUserRepository extends JpaRepository<RecordPerUser, Long> {
    List<RecordPerUser> findByUserCode_UserCode(UserEntity userCode);
    List<RecordPerUser> findByUserCode_UserCodeAndDayOfExercise(UserEntity userCode, LocalDate dayOfExercise);

    boolean existsByUserCode_UserCode(UserEntity userCode);
}
