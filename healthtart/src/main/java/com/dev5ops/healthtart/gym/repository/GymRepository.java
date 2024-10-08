package com.dev5ops.healthtart.gym.repository;

import com.dev5ops.healthtart.gym.aggregate.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRepository extends JpaRepository<Gym, Long> {
}
