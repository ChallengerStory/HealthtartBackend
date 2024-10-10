package com.dev5ops.healthtart.user.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestLoginVO {
    private String userEmail;
    private String userPassword;
}
