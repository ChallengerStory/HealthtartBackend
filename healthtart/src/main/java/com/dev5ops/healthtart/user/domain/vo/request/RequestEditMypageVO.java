package com.dev5ops.healthtart.user.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestEditMypageVO {

    private String userName;
    private String userEmail;
    private String userPassword;
    private String userPhone;
    private String userNickname;
    private String userGender;
    private Double userHeight;
    private Double userWeight;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
