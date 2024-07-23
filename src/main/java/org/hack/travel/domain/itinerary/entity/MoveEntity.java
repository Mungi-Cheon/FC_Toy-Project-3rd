package org.hack.travel.domain.itinerary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hack.travel.domain.itinerary.api.KakaoMapApi;
import org.hack.travel.domain.itinerary.dto.request.MoveRequest;
import org.hack.travel.global.converter.DateTimeConverter;


@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PRIVATE)
public class MoveEntity {

    @Column(name = "departure_place")
    private String departurePlace;

    @Column(name = "departure_place_road_address_name")
    private String departurePlaceAddress;

    private String destination;

    @Column(name = "destination_road_address_name")
    private String destinationAddress;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    private String transportation;

    public static MoveEntity of(MoveRequest req) {
        MoveEntity entity = new MoveEntity();
        entity.setDeparturePlace(req.getDeparturePlace());
        entity.setDeparturePlaceAddress(KakaoMapApi.getAddressByKeyword(req.getDeparturePlace()));
        entity.setDestination(req.getDestination());
        entity.setDestinationAddress(KakaoMapApi.getAddressByKeyword(req.getDestination()));
        entity.setDepartureTime(DateTimeConverter.toLocalDateTime(req.getDepartureTime()));
        entity.setArrivalTime(DateTimeConverter.toLocalDateTime(req.getArrivalTime()));
        entity.setTransportation(req.getTransportation());
        return entity;
    }

    public void update(MoveRequest req) {
        this.setDeparturePlace(req.getDeparturePlace());
        this.setDeparturePlaceAddress(KakaoMapApi.getAddressByKeyword(req.getDeparturePlace()));
        this.setDestination(req.getDestination());
        this.setDestinationAddress(KakaoMapApi.getAddressByKeyword(req.getDestination()));
        this.setDepartureTime(DateTimeConverter.toLocalDateTime(req.getDepartureTime()));
        this.setArrivalTime(DateTimeConverter.toLocalDateTime(req.getArrivalTime()));
        this.setTransportation(req.getTransportation());
    }
}
