package org.hack.travel.domain.like.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.exception.TripException;
import org.hack.travel.global.exception.UserException;
import org.hack.travel.domain.like.entity.LikeEntity;
import org.hack.travel.domain.like.repository.LikeRepository;
import org.hack.travel.domain.trip.entity.TripEntity;
import org.hack.travel.domain.trip.repository.TripRepository;
import org.hack.travel.domain.user.entity.UserEntity;
import org.hack.travel.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TripRepository tripRepository;

    @Captor
    private ArgumentCaptor<LikeEntity> likeEntityCaptor;
    private static final String EXIST_USER_EMAIL = "test1@gmail.com";
    private static final String NOT_EXIST_USER_EMAIL = "notexist@gmail.com";
    private static final int TRIP_ID = 99;
    private static final int USER_ID = 99;
    private static final int NOT_EXIST_TRIP_ID = 9999;

    @Test
    @Transactional
    void like_Success_Test() {
        UserEntity userEntity = UserEntity.builder().id(USER_ID).email(EXIST_USER_EMAIL).build();
        TripEntity tripEntity = TripEntity.builder().id(TRIP_ID).name("테스트")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1L)).domestic(false).build();

        when(userRepository.findByEmail(EXIST_USER_EMAIL)).thenReturn(Optional.of(userEntity));
        when(tripRepository.findById(TRIP_ID)).thenReturn(Optional.of(tripEntity));

        likeService.like(TRIP_ID, EXIST_USER_EMAIL);

        verify(likeRepository).save(likeEntityCaptor.capture());
        LikeEntity capturedLikeEntity = likeEntityCaptor.getValue();

        verify(likeRepository).save(any(LikeEntity.class));
        verify(userRepository).findByEmail(EXIST_USER_EMAIL);
        verify(tripRepository).findById(TRIP_ID);
        assertNotNull(capturedLikeEntity);
        assertEquals(tripEntity, capturedLikeEntity.getTripEntity());
        assertEquals(userEntity, capturedLikeEntity.getUserEntity());
    }

    @Test
    @Transactional
    void like_NotExist_User_Test() {
        when(userRepository.findByEmail(NOT_EXIST_USER_EMAIL)).thenReturn(Optional.empty());
        UserException exception = assertThrows(UserException.class, () -> {
            likeService.like(TRIP_ID, NOT_EXIST_USER_EMAIL);
        });
        assertEquals(String.format(MessageType.USER_NOT_FOUND.getMessage(),
            NOT_EXIST_USER_EMAIL), exception.getMessage());
        verify(likeRepository, never()).save(any(LikeEntity.class));
    }

    @Test
    @Transactional
    void like_NotExist_Trip_Test() {
        UserEntity userEntity = mock(UserEntity.class);
        when(userRepository.findByEmail(EXIST_USER_EMAIL)).thenReturn(Optional.of(userEntity));
        when(tripRepository.findById(NOT_EXIST_TRIP_ID)).thenReturn(Optional.empty());
        TripException exception = assertThrows(TripException.class, () -> {
            likeService.like(NOT_EXIST_TRIP_ID, EXIST_USER_EMAIL);
        });
        assertEquals(
            String.format(MessageType.ID_NOT_FOUND.getMessage(), MessageAffixType.TRIP.getType()),
            exception.getMessage());
        verify(likeRepository, never()).save(any(LikeEntity.class));
    }
}