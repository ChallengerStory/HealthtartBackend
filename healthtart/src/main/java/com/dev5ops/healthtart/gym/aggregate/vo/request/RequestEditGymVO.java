package com.dev5ops.healthtart.gym.aggregate.vo.request;

import com.dev5ops.healthtart.equipment_per_gym.aggregate.EquipmentPerGym;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestEditGymVO {
    private String gymName;
    private String address;
    private String businessNumber;
}
