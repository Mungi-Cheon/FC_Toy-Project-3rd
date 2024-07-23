package org.hack.travel.global.util;

import java.time.LocalDateTime;
import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.converter.DateTimeConverter;
import org.hack.travel.global.exception.ItineraryException;
import org.hack.travel.domain.itinerary.dto.request.ItineraryRequest;
import org.hack.travel.domain.trip.entity.TripEntity;

public class ItineraryValidationUtil {

    public static void validateItinerary(TripEntity tripEntity, ItineraryRequest itineraryRequest) {
        var tripStartDate = tripEntity.getStartDate();
        var tripEndDate = tripEntity.getEndDate();

        if (!isValidRequest(itineraryRequest.getStay().getStartTime(),
            itineraryRequest.getStay().getEndTime())) {
            throw new ItineraryException(MessageAffixType.STAY_END,
                MessageType.MUST_BE_AFTER, MessageAffixType.STAY_START);
        }

        if (!isValidRequest(itineraryRequest.getMove().getDepartureTime(),
            itineraryRequest.getMove().getArrivalTime())) {
            throw new ItineraryException(MessageAffixType.MOVE_END,
                MessageType.MUST_BE_AFTER, MessageAffixType.MOVE_START);
        }

        if (!isValidRequest(
            itineraryRequest.getAccommodation().getCheckIn(),
            itineraryRequest.getAccommodation().getCheckOut())) {
            throw new ItineraryException(MessageAffixType.CHECK_OUT,
                MessageType.MUST_BE_AFTER, MessageAffixType.CHECK_IN);
        }

        if (!isValidTimeline(tripEntity, itineraryRequest)) {
            throw new ItineraryException(
                MessageType.ITINERARY_DATE_MUST_BE_BETWEEN_START_AND_END);
        }
    }

    private static boolean isValidRequest(String startTime, String endTime) {
        return DateTimeConverter.toLocalDateTime(startTime)
            .isBefore(DateTimeConverter.toLocalDateTime(endTime));
    }

    private static boolean isValidTimeline(TripEntity tripEntity,
        ItineraryRequest itineraryRequest) {

        LocalDateTime adjustedTripEndDateTime = tripEntity.getEndDate().plusDays(1).atStartOfDay()
            .minusSeconds(1);

        LocalDateTime moveDepartureTime = DateTimeConverter.toLocalDateTime(
            itineraryRequest.getMove().getDepartureTime());
        LocalDateTime moveArrivalTime = DateTimeConverter.toLocalDateTime(
            itineraryRequest.getMove().getArrivalTime());

        LocalDateTime checkIn = DateTimeConverter.toLocalDateTime(
            itineraryRequest.getAccommodation().getCheckIn());
        LocalDateTime checkOut = DateTimeConverter.toLocalDateTime(
            itineraryRequest.getAccommodation().getCheckOut());

        LocalDateTime stayStartTime = DateTimeConverter.toLocalDateTime(
            itineraryRequest.getStay().getStartTime());
        LocalDateTime stayEndTime = DateTimeConverter.toLocalDateTime(
            itineraryRequest.getStay().getEndTime());

        final boolean isTripStartBeforeOrAtMove =
            tripEntity.getStartDate().atStartOfDay().isBefore(moveDepartureTime)
                || tripEntity.getStartDate().atStartOfDay().isEqual(moveDepartureTime);

        final boolean isBeforeOrEqualCheckIn =
            moveArrivalTime.isBefore(checkIn) || moveArrivalTime.isEqual(checkIn);

        final boolean isBeforeOrEqualStayStart =
            moveArrivalTime.isBefore(stayStartTime) || moveArrivalTime.isEqual(stayStartTime);

        final boolean isAfterOrEqualStayEnd = (adjustedTripEndDateTime.isAfter(stayEndTime)
            || adjustedTripEndDateTime.isEqual(stayEndTime));

        final boolean isAfterOrEqualCheckOut = (adjustedTripEndDateTime.isAfter(checkOut)
            || adjustedTripEndDateTime.isEqual(
            checkOut));

        return isTripStartBeforeOrAtMove
            && isBeforeOrEqualCheckIn
            && isBeforeOrEqualStayStart
            && isAfterOrEqualStayEnd
            && isAfterOrEqualCheckOut;
    }
}


