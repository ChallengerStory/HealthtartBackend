package com.dev5ops.healthtart.gym.aggregate.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class RequestEditGymVO {
    private String gymName;
    private String address;
    private String businessNumber;
    private LocalDateTime updatedAt;
}
