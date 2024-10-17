package com.dev5ops.healthtart.user.domain.vo.response;

import com.dev5ops.healthtart.user.domain.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ResponseMypageVO {

    private String userName;
    private String userEmail;
    private String userPassword;
    private String userPhone;
    private String userNickname;
    private String userGender;
    private Double userHeight;
    private Double userWeight;
    private LocalDateTime updatedAt;
}
