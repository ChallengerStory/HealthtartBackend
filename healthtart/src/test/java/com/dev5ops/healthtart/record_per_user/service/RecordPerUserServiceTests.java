package com.dev5ops.healthtart.record_per_user.service;


import com.dev5ops.healthtart.record_per_user.aggregate.RecordPerUser;
import com.dev5ops.healthtart.record_per_user.dto.ResponseRecordPerUserVO;
import com.dev5ops.healthtart.record_per_user.repository.RecordPerUserRepository;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RecordPerUserServiceTests {

    @Mock
    private RecordPerUserRepository recordPerUserRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RecordPerUserService recordPerUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("유저별 운동 기록 조회 성공")
    @Test
    void findRecordByUserCode_Success() {

        // given
        String userCode = "testUserCode";
        UserEntity mockUser = UserEntity.builder()
                .userCode(userCode)
                .build();

        // 유저별 운동기록은 여러개일 수 있으니 2개의 기록으로 테스트
        RecordPerUser mockFirstRecordPerUser = RecordPerUser.builder()
                .userRecordCode(1L)
                .dayOfExercise(LocalDate.of(2024,10,9))
                .exerciseDuration(LocalTime.of(1,0,0))
                .recordFlag(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userCode(mockUser)
                .workoutPerRoutineCode(1L)
                .build();

        RecordPerUser mockSecondRecordPerUser = RecordPerUser.builder()
                .userRecordCode(2L)
                .dayOfExercise(LocalDate.of(2024,10,10))
                .exerciseDuration(LocalTime.of(1,0,0))
                .recordFlag(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userCode(mockUser)
                .workoutPerRoutineCode(1L)
                .build();

        List<RecordPerUser> mockRecordsPerUser = Arrays
                .asList(mockFirstRecordPerUser, mockSecondRecordPerUser);

        ResponseRecordPerUserVO mockFirstRecordPerUserDTO = new ResponseRecordPerUserVO(1L
                ,LocalDate.of(2024,10,9)
                ,LocalTime.of(1,0,0)
                ,true
                ,LocalDateTime.now()
                ,LocalDateTime.now()
                ,mockUser
                ,1L);

        ResponseRecordPerUserVO mockSecondRecordPerUserDTO = new ResponseRecordPerUserVO(2L
                ,LocalDate.of(2024,10,10)
                ,LocalTime.of(1,0,0)
                ,true
                ,LocalDateTime.now()
                ,LocalDateTime.now()
                ,mockUser
                ,1L);

        List<ResponseRecordPerUserVO> mockRecordsPerUserDTO = Arrays
                .asList(mockFirstRecordPerUserDTO, mockSecondRecordPerUserDTO);

        // when
        when(recordPerUserRepository.findByUserCode_UserCode(userCode))
                .thenReturn(mockRecordsPerUser);
        when(modelMapper.map(mockFirstRecordPerUser, ResponseRecordPerUserVO.class))
                .thenReturn(mockFirstRecordPerUserDTO);
        when(modelMapper.map(mockSecondRecordPerUser, ResponseRecordPerUserVO.class))
                .thenReturn(mockSecondRecordPerUserDTO);

        List<ResponseRecordPerUserVO> actual = recordPerUserService.findRecordByUserCode(userCode);

        // then
        assertNotNull(actual);
        assertEquals(mockRecordsPerUserDTO, actual);

        verify(recordPerUserRepository, times(1)).findByUserCode_UserCode(userCode);

        // any - RecordPerUser의 어떤 객체여도 상관 없다 / eq - RecordPerUserDTO여야만 한다
        verify(modelMapper, times(2)).map(any(RecordPerUser.class), eq(ResponseRecordPerUserVO.class));

    }

    // findByUserCode_fail 테스트코드 작성

    @DisplayName("날짜별 운동 기록 조회 성공")
    @Test
    void findRecordPerDate_Success() {

        // given
        String userCode = "testUserCode";
        UserEntity mockUser = UserEntity.builder()
                .userCode(userCode)
                .build();

        LocalDate dayOfExercise = LocalDate.of(2024, 10, 9);

        RecordPerUser firstMockRecordPerUserAndDay = RecordPerUser.builder()
                .userRecordCode(1L)
                .dayOfExercise(LocalDate.of(2024,10,9))
                .exerciseDuration(LocalTime.of(1,0,0))
                .recordFlag(true)
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .userCode(mockUser)
                .workoutPerRoutineCode(1L)
                .build();

        RecordPerUser secondMockRecordPerUserAndDay = RecordPerUser.builder()
                .userRecordCode(2L)
                .dayOfExercise(LocalDate.of(2024,10,9))
                .exerciseDuration(LocalTime.of(1,0,0))
                .recordFlag(true)
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .userCode(mockUser)
                .workoutPerRoutineCode(1L)
                .build();


        List<RecordPerUser> mockRecordsPerUserAndDay = Arrays
                .asList(firstMockRecordPerUserAndDay, secondMockRecordPerUserAndDay);


        ResponseRecordPerUserVO mockFirstRecordPerUserAndDayDTO = new ResponseRecordPerUserVO(1L
                ,LocalDate.of(2024,10,9)
                ,LocalTime.of(1,0,0)
                ,true
                ,LocalDateTime.now().withNano(0)
                ,LocalDateTime.now().withNano(0)
                ,mockUser
                ,1L);

        ResponseRecordPerUserVO mockSecondRecordPerUserAndDayDTO = new ResponseRecordPerUserVO(2L
                ,LocalDate.of(2024,10,9)
                ,LocalTime.of(1,0,0)
                ,true
                ,LocalDateTime.now().withNano(0)
                ,LocalDateTime.now().withNano(0)
                ,mockUser
                ,1L);


        List<ResponseRecordPerUserVO> mockRecordsPerUserAndDayDTO = Arrays
                .asList(mockFirstRecordPerUserAndDayDTO, mockSecondRecordPerUserAndDayDTO);

        // when
        when(recordPerUserRepository.findByUserCode_UserCodeAndDayOfExercise(userCode, dayOfExercise))
                .thenReturn(mockRecordsPerUserAndDay);
        when(modelMapper.map(firstMockRecordPerUserAndDay, ResponseRecordPerUserVO.class))
                .thenReturn(mockFirstRecordPerUserAndDayDTO);
        when(modelMapper.map(secondMockRecordPerUserAndDay, ResponseRecordPerUserVO.class))
                .thenReturn(mockSecondRecordPerUserAndDayDTO);

        List<ResponseRecordPerUserVO> actual = recordPerUserService.findRecordPerDate(userCode, dayOfExercise);

        // then
        assertNotNull(actual);
        assertEquals(mockRecordsPerUserAndDayDTO, actual);

        verify(recordPerUserRepository, times(1)).findByUserCode_UserCodeAndDayOfExercise(userCode, dayOfExercise);
        verify(modelMapper, times(2)).map(any(RecordPerUser.class), eq(ResponseRecordPerUserVO.class));

    }


}