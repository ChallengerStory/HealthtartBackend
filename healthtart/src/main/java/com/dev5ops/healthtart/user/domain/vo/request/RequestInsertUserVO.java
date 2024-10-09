package com.dev5ops.healthtart.user.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 회원가입시 유저가 입력하는 정보
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestInsertUserVO {

    // String userCode;
    String userType; // 이미 정해진 정보
    String userName;
    String userEmail;
    String userPassword;
    String userPhone;
    String nickname;
    String userAddress;
    // Boolean userFlag;
    String userGender;
    Double userHeight;
    Double userWeight;
    Integer userAge;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    // Integer gymCode;

    public void changePwd(String encodedPwd){
        this.userPassword = encodedPwd;
    }
}
