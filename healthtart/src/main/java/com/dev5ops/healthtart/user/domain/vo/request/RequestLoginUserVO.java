package com.dev5ops.healthtart.user.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestLoginUserVO {
    private String userEmail;
    private String userPassword;
}
