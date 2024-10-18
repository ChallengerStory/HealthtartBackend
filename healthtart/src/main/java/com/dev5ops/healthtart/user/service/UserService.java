package com.dev5ops.healthtart.user.service;

import com.dev5ops.healthtart.user.domain.dto.*;
import com.dev5ops.healthtart.user.domain.vo.request.RequestInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.request.RequestOauth2VO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    ResponseInsertUserDTO signUpUser(RequestInsertUserVO request);

    List<UserDTO> findAllUsers();

    UserDTO findUserByEmail(String userEmail);

    UserDTO findById(String userCode);

    void deleteUser(String userCode);

    Boolean checkDuplicateNickname(String userNickname);

    void saveOauth2User(RequestOauth2VO requestOauth2VO);

    ResponseMypageDTO getMypageInfo();

    void editMypageInfo(EditMypageDTO editUserDTO);
}
