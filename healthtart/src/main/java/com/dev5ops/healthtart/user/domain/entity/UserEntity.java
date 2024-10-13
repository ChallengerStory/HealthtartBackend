package com.dev5ops.healthtart.user.domain.entity;

import com.dev5ops.healthtart.user.domain.UserTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "UserEntity")
@Table(name = "users")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserEntity {

    @Id
    @Column(name = "user_code", nullable = false, unique = true)
    private String userCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserTypeEnum userType;

    // 동명이인이 있을 수도 있으니까 unique = true는 없애는게 좋지 않을까?
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_password"/*, nullable = false*/)
    private String userPassword;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Column(name = "user_nickname", nullable = false)
    private String userNickname;

    @Column(name = "user_address", nullable = false)
    private String userAddress;

    @Column(name = "user_flag", nullable = false)
    private Boolean userFlag;

    @Column(name = "user_gender")
    private String userGender;

    @Column(name = "user_height")
    private Double userHeight;

    @Column(name = "user_weight")
    private Double userWeight;

    @Column(name = "user_age")
    private Integer userAge;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

//    @ManyToOne
//    @JoinColumn(name = "gym_code", nullable = true)
    @Column(name = "gym_code", nullable = true)
    private Long gymCode;
}
