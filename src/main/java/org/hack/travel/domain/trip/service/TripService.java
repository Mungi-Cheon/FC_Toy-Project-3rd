package org.hack.travel.domain.trip.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hack.travel.global.exception.UserException;
import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.exception.TripException;
import org.hack.travel.global.util.KeywordValidationUtil;
import org.hack.travel.global.util.UserValidationUtil;
import org.hack.travel.domain.trip.dto.request.TripRequest;
import org.hack.travel.domain.trip.dto.response.TripResponse;
import org.hack.travel.domain.trip.dto.response.TripSimpleResponse;
import org.hack.travel.domain.trip.entity.TripEntity;
import org.hack.travel.domain.trip.repository.TripRepository;
import org.hack.travel.domain.user.entity.UserEntity;
import org.hack.travel.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripResponse save(String email, TripRequest tripRequest) {
        UserEntity userEntity = getUserEntity(email);
        return TripResponse.fromEntity(tripRepository.save(TripEntity.of(tripRequest, userEntity)));
    }

    @Transactional(readOnly = true)
    public List<TripSimpleResponse> findAllTrips() {
        return tripRepository.findAllTripJoinLike().stream().map(TripSimpleResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripResponse findTripById(int tripId) {
        return TripResponse.fromEntity(tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP)));
    }

    @Transactional
    public TripResponse updateTripById(String email, TripRequest tripRequest, int tripId) {
        UserEntity userEntity = getUserEntity(email);

        TripEntity oldTripEntity = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP));

        if (!UserValidationUtil.isEqualUser(userEntity.getId(), oldTripEntity.getUserEntity().getId())) {
            throw new UserException();
        }
        return TripResponse.fromEntity(tripRepository.save(
            oldTripEntity.update(tripRequest, userEntity)));
    }

    @Transactional
    public void deleteTripById(String email, int tripId) {
        UserEntity userEntity = getUserEntity(email);
        TripEntity tripEntity = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP));

        if (!UserValidationUtil.isEqualUser(userEntity.getId(), tripEntity.getUserEntity().getId())) {
            throw new UserException();
        }
        tripRepository.deleteByIdAndUserEntityId(tripId, userEntity.getId());
    }

    @Transactional(readOnly = true)
    public List<TripResponse> findTripsByKeyword(String keyword) {
        KeywordValidationUtil.validateKeyword(keyword);
        List<TripEntity> tripList = tripRepository.findByNameContains(keyword);
        return tripList.stream().map(TripResponse::fromEntity).collect(Collectors.toList());
    }

    private UserEntity getUserEntity(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(MessageType.USER_NOT_FOUND));
    }
}
