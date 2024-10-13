package com.dev5ops.healthtart.rival.domain.dto.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RivalUserEntityDTO {

    // 유저 정보
    private String userName;

    private String userGender;

    private Double userHeight;

    private Double userWeight;

    private Integer userAge;


    // 인바디 정보
    //    private Long inbodyCode;
    private Integer inbodyScore;
    private double weight;
    private double height;
    private double muscleWeight;
    private double fatWeight;
    private double bmi;
    private double fatPercentage;
//    private LocalDateTime dayOfInbody;
    private Integer basalMetabolicRate;
}
