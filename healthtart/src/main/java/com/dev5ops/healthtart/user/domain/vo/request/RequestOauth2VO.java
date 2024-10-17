package com.dev5ops.healthtart.user.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestOauth2VO {

    String userName;
//    String userType;
    String userEmail;
//    String userPassword;
    String userPhone;
    String userNickname;
    String userAddress;
    // Boolean userFlag;
    String userGender;
    Double userHeight;
    Double userWeight;
    Integer userAge;
//    LocalDateTime createdAt;
//    LocalDateTime updatedAt;
    String provider;
    String providerId;
    // Integer gymCode;
}
