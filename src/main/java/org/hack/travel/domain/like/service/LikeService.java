package org.hack.travel.domain.like.service;

import java.lang.management.MemoryPoolMXBean;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.exception.LikeException;
import org.hack.travel.global.exception.TripException;
import org.hack.travel.global.exception.UserException;
import org.hack.travel.global.util.UserValidationUtil;
import org.hack.travel.domain.like.entity.LikeEntity;
import org.hack.travel.domain.like.repository.LikeRepository;
import org.hack.travel.domain.trip.dto.response.TripSimpleResponse;
import org.hack.travel.domain.trip.entity.TripEntity;
import org.hack.travel.domain.trip.repository.TripRepository;
import org.hack.travel.domain.user.entity.UserEntity;
import org.hack.travel.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public void like(int tripId, String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(MessageType.USER_NOT_FOUND, email));

        TripEntity tripEntity = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP));

        Optional<LikeEntity> entity = likeRepository.findLikeEntityByUserIdAndTripId(
            userEntity.getId(), tripEntity.getId());
        if (entity.isPresent()) {
            throw new LikeException(MessageType.LIKE_ERR_ALREADY_EXIST_LIKE);
        }

        LikeEntity likeEntity = LikeEntity.of(tripEntity, userEntity);
        likeRepository.save(likeEntity);
    }

    public Integer getLikeCount(int tripId) {
        TripEntity tripEntity = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP));
        return likeRepository.findLikeCountByTripId(tripEntity.getId());
    }

    public List<TripSimpleResponse> findAllTripByLiked(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(MessageType.USER_NOT_FOUND));
        List<LikeEntity> likeList = userEntity.getLikeList();
        if (likeList.isEmpty()) {
            throw new RuntimeException("No have liked trips"); // 예외가 아니라 Response로 보내기
        }
        return likeList.stream().map(like -> TripSimpleResponse.fromEntity(like.getTripEntity()))
            .toList();
    }

    @Transactional
    public void deleteLike(int tripId, String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(MessageType.USER_NOT_FOUND));
        TripEntity tripEntity = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP));
        LikeEntity likeEntity = likeRepository.findLikeEntityByUserIdAndTripId(userEntity.getId(),
                tripId)
            .orElseThrow(() -> new LikeException(MessageType.LIKE_ERR_NOT_FOUND));

        if (!UserValidationUtil.isEqualUser(userEntity.getId(), tripEntity.getUserEntity().getId())) {
            throw new LikeException(MessageType.NOT_EQUAL_USER);
        }
        likeRepository.deleteLikeById(likeEntity.getId());
    }
}
