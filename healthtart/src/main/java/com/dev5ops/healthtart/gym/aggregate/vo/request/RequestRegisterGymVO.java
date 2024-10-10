package com.dev5ops.healthtart.gym.aggregate.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestRegisterGymVO {
    private String gymName;
    private String address;
    private String businessNumber;
}
