package com.dev5ops.healthtart.record_per_user.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.record_per_user.aggregate.RecordPerUser;
import com.dev5ops.healthtart.record_per_user.aggregate.vo.request.RequestRegistRecordPerUserVO;
import com.dev5ops.healthtart.record_per_user.dto.ResponseRecordPerUserVO;
import com.dev5ops.healthtart.record_per_user.repository.RecordPerUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service("recordPerUserService")
public class RecordPerUserService {
    private final RecordPerUserRepository recordPerUserRepository;
    private final ModelMapper modelMapper;

    public List<ResponseRecordPerUserVO> findRecordByUserCode(String UserCode) {
        List<RecordPerUser> recordPerUser = recordPerUserRepository.findByUserCode_UserCode(UserCode);

        if (recordPerUser.isEmpty()) {
            throw new CommonException(StatusEnum.USER_NOT_FOUND);
        }

        return recordPerUser.stream()
                .filter(RecordPerUser::isRecordFlag)
                .map(record -> modelMapper.map(record, ResponseRecordPerUserVO.class))
                .collect(Collectors.toList());
    }

    public List<ResponseRecordPerUserVO> findRecordPerDate(String UserCode, LocalDate dayOfExercise) {
        List<RecordPerUser> recordPerUser = recordPerUserRepository
                .findByUserCode_UserCodeAndDayOfExercise(UserCode, dayOfExercise);

        if (recordPerUser.isEmpty()) {
            boolean userExists = recordPerUserRepository.existsByUserCode_UserCode(UserCode);

            if (!userExists) {
                throw new CommonException(StatusEnum.USER_NOT_FOUND);
            }
            throw new CommonException(StatusEnum.DAY_NOT_FOUND);
        }

        return recordPerUser.stream()
                .filter(RecordPerUser::isRecordFlag)
                .map(record -> modelMapper.map(record, ResponseRecordPerUserVO.class))
                .collect(Collectors.toList());
    }

    public ResponseRecordPerUserVO registerRecordPerUser(RequestRegistRecordPerUserVO requestRegistRecordPerUserVO){
        RecordPerUser recordPerUser = modelMapper.map(requestRegistRecordPerUserVO, RecordPerUser.class);
        recordPerUser = recordPerUserRepository.save(recordPerUser);
        return modelMapper.map(recordPerUser, ResponseRecordPerUserVO.class);
    }

    // 운동 기록을 수정할 일이 없는거 같음
//    public RecordPerUserDTO editRecordPerUser(Long userRecordCode, RequestEditRecordPerUserVO request){
//        RecordPerUser recordPerUser = recordPerUserRepository
//                .findById(userRecordCode).orElseThrow(() -> new CommonException(StatusEnum.RECORD_NOT_FOUND));
//
//        recordPerUser.setExerciseDuration(request.getExerciseDuration());
//        recordPerUser.setUpdatedAt(request.getUpdatedAt());
//
//        recordPerUser = recordPerUserRepository.save(recordPerUser);
//
//        return modelMapper.map(recordPerUser, RecordPerUserDTO.class);
//    }

    public void deleteRecordPerUser(Long userRecordCode) {
        RecordPerUser recordPerUser = recordPerUserRepository
                .findById(userRecordCode).orElseThrow(() -> new CommonException(StatusEnum.RECORD_NOT_FOUND));

        recordPerUserRepository.delete(recordPerUser);
    }



}
