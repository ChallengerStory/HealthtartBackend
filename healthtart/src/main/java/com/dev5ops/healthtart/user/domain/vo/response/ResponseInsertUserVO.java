package com.dev5ops.healthtart.user.domain.vo.response;

import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseInsertUserVO {

    private UserDTO newUser;
}
