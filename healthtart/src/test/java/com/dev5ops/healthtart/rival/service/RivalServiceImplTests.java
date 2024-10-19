package com.dev5ops.healthtart.rival.service;

import com.dev5ops.healthtart.common.exception.CommonException;
import com.dev5ops.healthtart.rival.domain.dto.RivalDTO;
import com.dev5ops.healthtart.rival.domain.entity.Rival;
import com.dev5ops.healthtart.rival.repository.RivalRepository;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import com.dev5ops.healthtart.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RivalServiceImplTests {

    @InjectMocks
    private RivalServiceImpl rivalService;

    @Mock
    private RivalRepository rivalRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("내 라이벌 조회 서비스 테스트")
    void testFindRivalMatch() {
        // Given: 임의의 userCode와 RivalDTO 준비
        String userCode = "test-user-code";

        // RivalDTO 객체 생성
        RivalDTO rivalDTO = RivalDTO.builder()
                .rivalMatchCode(1L)
                .userCode(userCode)
                .rivalUserCode("rival-user-code")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Mock 설정: rivalRepository에서 RivalDTO 반환
        when(rivalRepository.findByUser_UserCode(userCode)).thenReturn(rivalDTO);

        // Mock 설정: getUserCode() 메서드가 userCode를 반환하도록 설정
        RivalServiceImpl rivalServiceSpy = spy(rivalService);
        doReturn(userCode).when(rivalServiceSpy).getUserCode();

        // When: 서비스의 findRivalMatch 호출
        RivalDTO actualRivalDTO = rivalServiceSpy.findRivalMatch();

        // Then: 결과 검증
        assertNotNull(actualRivalDTO);  // 반환된 값이 null이 아닌지 확인
        assertEquals(1L, actualRivalDTO.getRivalMatchCode());
        assertEquals(userCode, actualRivalDTO.getUserCode());
        assertEquals("rival-user-code", actualRivalDTO.getRivalUserCode());

        // findByUser_UserCode가 정확하게 호출되었는지 검증
        verify(rivalRepository, times(1)).findByUser_UserCode(userCode);
    }




}