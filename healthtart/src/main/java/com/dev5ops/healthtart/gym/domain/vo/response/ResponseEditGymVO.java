package com.dev5ops.healthtart.gym.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseEditGymVO {
    private String gymName;
    private String address;
    private String businessNumber;
    private LocalDateTime updatedAt;
}
