package com.dev5ops.healthtart.user.service;

import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import com.dev5ops.healthtart.user.domain.vo.request.RequestInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseInsertUserVO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    ResponseInsertUserVO signUpUser(RequestInsertUserVO request);

    List<UserDTO> findAllUsers();

    UserDTO findUserByEmail(String userEmail);

    UserDTO findById(String userCode);

    void deleteUser(String userCode);
}
