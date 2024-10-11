package com.dev5ops.healthtart.record_per_user.repository;

import com.dev5ops.healthtart.record_per_user.aggregate.RecordPerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordPerUserRepository extends JpaRepository<RecordPerUser, Long> {
    List<RecordPerUser> findByUserCode_UserCode(String userCode);
    List<RecordPerUser> findByUserCode_UserCodeAndDayOfExercise(String userCode, LocalDate dayOfExercise);

    boolean existsByUserCode_UserCode(String userCode);
}
