package com.dev5ops.healthtart.inbody.aggregate.vo.response;

import com.dev5ops.healthtart.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseRegisterInbodyVO {
    private Long inbodyCode;
    private Integer inbodyScore;
    private double weight;
    private double height;
    private double muscleWeight;
    private double fatWeight;
    private double bmi;
    private double fatPercentage;
    private LocalDateTime dayOfInbody;
    private Integer basalMetabolicRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User user;
}
