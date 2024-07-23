package org.hack.travel.domain.itinerary.service;

import lombok.RequiredArgsConstructor;
import org.hack.travel.domain.user.entity.UserEntity;
import org.hack.travel.domain.user.repository.UserRepository;
import org.hack.travel.global.converter.DateTimeConverter;
import org.hack.travel.global.exception.ItineraryException;
import org.hack.travel.global.exception.TripException;
import org.hack.travel.global.exception.UserException;
import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.util.ItineraryValidationUtil;
import org.hack.travel.domain.itinerary.api.KakaoMapApi;
import org.hack.travel.domain.itinerary.dto.request.MoveRequest;
import org.hack.travel.domain.itinerary.dto.request.StayRequest;
import org.hack.travel.domain.itinerary.entity.ItineraryEntity;
import org.hack.travel.domain.itinerary.dto.response.ItineraryResponse;
import org.hack.travel.domain.itinerary.entity.MoveEntity;
import org.hack.travel.domain.itinerary.entity.StayEntity;
import org.hack.travel.domain.itinerary.repository.ItineraryRepository;
import org.hack.travel.domain.itinerary.dto.request.ItineraryRequest;
import org.hack.travel.domain.trip.entity.TripEntity;
import org.hack.travel.domain.trip.repository.TripRepository;
import org.hack.travel.global.util.UserValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    @Transactional
    public ItineraryResponse createItinerary(ItineraryRequest itineraryRequest, String email) {
        UserEntity userEntity = getUserEntity(email);
        TripEntity tripEntity = tripRepository.findById(itineraryRequest.getTripId())
            .orElseThrow(
                () -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP));

        if (!UserValidationUtil.isEqualUser(userEntity.getId(),
            tripEntity.getUserEntity().getId())) {
            throw new ItineraryException(MessageType.TRIP_CREATION_RESTRICTION_MESSAGE);
        }

        ItineraryValidationUtil.validateItinerary(tripEntity, itineraryRequest);
        return ItineraryResponse.fromEntity(
            itineraryRepository.save(ItineraryEntity.of(itineraryRequest, tripEntity)));
    }

    public ItineraryResponse findItineraryByTripId(int tripId, int itineraryId) {
        TripEntity tripEntity = tripRepository.findById(tripId)
            .orElseThrow(
                () -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP));
        ItineraryEntity itineraryEntity = tripEntity.getItineraries().stream()
            .filter(itEntity -> itEntity.getId().equals(itineraryId)).findFirst().orElseThrow(() ->
                new ItineraryException(MessageType.ID_NOT_FOUND,
                    MessageAffixType.ITINERARY));

        return ItineraryResponse.fromEntity(itineraryEntity);
    }

    @Transactional
    public ItineraryResponse updateItinerary(int tripId, int itineraryId,
        ItineraryRequest updatedRequest, String email) {
        UserEntity userEntity = getUserEntity(email);
        TripEntity tripEntity = tripRepository.findById(tripId)
            .orElseThrow(
                () -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP));

        if (!UserValidationUtil.isEqualUser(userEntity.getId(),
            tripEntity.getUserEntity().getId())) {
            throw new UserException();
        }

        ItineraryValidationUtil.validateItinerary(tripEntity, updatedRequest);

        ItineraryEntity oldItineraryEntity = tripEntity.getItineraries().stream()
            .filter(itinerary -> itinerary.getId().equals(itineraryId)).findFirst()
            .orElseThrow(
                () -> new ItineraryException(MessageType.ID_NOT_FOUND,
                    MessageAffixType.ITINERARY));

        ItineraryEntity newItineraryEntity = oldItineraryEntity.update(updatedRequest, tripEntity);
        return ItineraryResponse.fromEntity(itineraryRepository.save(newItineraryEntity));
    }

    @Transactional
    public void deleteItinerary(int tripId, int itineraryId, String email) {
        UserEntity userEntity = getUserEntity(email);
        TripEntity tripEntity = tripRepository.findByIdAndUserEntityId(tripId, 1)
            .orElseThrow(
                () -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP));

        if (!UserValidationUtil.isEqualUser(userEntity.getId(),
            tripEntity.getUserEntity().getId())) {
            throw new UserException();
        }

        tripEntity.getItineraries().stream()
            .filter(itEntity -> itEntity.getId().equals(itineraryId)).findFirst().orElseThrow(() ->
                new ItineraryException(MessageType.ID_NOT_FOUND,
                    MessageAffixType.ITINERARY));

        itineraryRepository.deleteById(itineraryId);
    }

    private UserEntity getUserEntity(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(MessageType.USER_NOT_FOUND));
    }
}
