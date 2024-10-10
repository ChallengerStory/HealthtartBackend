package com.dev5ops.healthtart.user.service;

import com.dev5ops.healthtart.user.domain.dto.UserDTO;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import com.dev5ops.healthtart.user.domain.vo.request.RequestInsertUserVO;
import com.dev5ops.healthtart.user.domain.vo.response.ResponseInsertUserVO;
import com.dev5ops.healthtart.user.repository.UserRepository;
import com.dev5ops.healthtart.user.domain.vo.request.RequestLogInVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public ResponseInsertUserVO signUpUser(RequestInsertUserVO request);

    UserDTO loginUser(String userEmail, String userPassword);

    public List<UserDTO> findAllUsers();

    public UserDetails findUserByUsername(String userEmail);


//    public void logInProcess(RequestLogInVO request) {
//
//        String userEmail = request.getUserEmail();
//        String userPassword = request.getUserPassword();
//
//        System.out.println(userEmail);
//
//        Boolean isExists = userRepository.existsByUserCode(userEmail);
//
//        if(isExists) {
//
//            return;
//        }
//        UserEntity data = new UserEntity();
//        data.setUserCode(userEmail);
//        data.setUserPassword(bCryptPasswordEncoder.encode(userPassword));
//
//        userRepository.save(data);
//    }
}