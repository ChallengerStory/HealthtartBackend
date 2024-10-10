package com.dev5ops.healthtart.gym.aggregate.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseFindGymVO {
    private String gymName;
    private String address;
    private String businessNumber;
}
