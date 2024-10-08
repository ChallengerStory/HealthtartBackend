package com.dev5ops.healthtart.user.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {

    private String userCode;

    private String userType;

    private String userName;

    private String userEmail;

    private String userPassword;

    private String userPhone;

    private String nickname;

    private String userAddress;

    private Boolean userFlag;

    private String userGender;

    private Double userHeight;

    private Double userWeight;

    private Integer userAge;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long gymCode;
}
