package com.dev5ops.healthtart.record_per_user.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.common.exception.StatusEnum;
import com.dev5ops.healthtart.record_per_user.aggregate.RecordPerUser;
import com.dev5ops.healthtart.record_per_user.dto.RecordPerUserDTO;
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

    // 조회 - (유저별운동기록)유저별 운동 기록 조회 - UserCode Join | 운동기록은 여러개 있을 수 있으니 List
    public List<RecordPerUserDTO> findRecordByUserCode(String UserCode) {
        List<RecordPerUser> recordPerUser = recordPerUserRepository.findByUser_UserCode(UserCode);

        // 유저가 존재하지 않는다면 USER_NOT_FOUND
        if (recordPerUser.isEmpty()) {
            throw new CommonException(StatusEnum.USER_NOT_FOUND);
        }

        return recordPerUser.stream()
                .filter(RecordPerUser::isRecordFlag)
                .map(record -> modelMapper.map(record, RecordPerUserDTO.class))
                .collect(Collectors.toList());
    }

    // (유저별운동기록)날짜별 운동 기록 조회 - UserCode Join | 하루에 여러번 운동 할 수 있으니 List
    public List<RecordPerUserDTO> findRecordPerDate(String UserCode, LocalDate dayOfExercise) {
        List<RecordPerUser> recordPerUser = recordPerUserRepository
                .findByUser_UserCodeAndDayOfExercise(UserCode, dayOfExercise);

        if (recordPerUser.isEmpty()) {
            boolean userExists = recordPerUserRepository.existsByUser_UserCode(UserCode);

            // 유저가 존재하지 않으면 USER_NOT_FOUND
            if (!userExists) {
                throw new CommonException(StatusEnum.USER_NOT_FOUND);
            }
            // 유저는 존재하지만 그 날 운동한 기록이 없다면 DAY_NOT_FOUND
            throw new CommonException(StatusEnum.DAY_NOT_FOUND);
        }

        return recordPerUser.stream()
                .filter(RecordPerUser::isRecordFlag)
                .map(record -> modelMapper.map(record, RecordPerUserDTO.class))
                .collect(Collectors.toList());
    }


}
