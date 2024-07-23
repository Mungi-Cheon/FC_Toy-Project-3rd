package org.hack.travel.domain.itinerary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hack.travel.domain.itinerary.api.KakaoMapApi;
import org.hack.travel.domain.itinerary.dto.request.AccommodationRequest;
import org.hack.travel.global.converter.DateTimeConverter;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PRIVATE)
public class AccommodationEntity {

    @Column(name = "accommodation")
    private String name;

    @Column(name = "accommodation_road_address_name")
    private String accommodationAddress;

    @Column(name = "check_in")
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    public static AccommodationEntity of(AccommodationRequest req) {
        AccommodationEntity entity = new AccommodationEntity();
        entity.setName(req.getAccommodationName());
        entity.setAccommodationAddress(KakaoMapApi.getAddressByKeyword(req.getAccommodationName()));
        entity.setCheckIn(DateTimeConverter.toLocalDateTime(req.getCheckIn()));
        entity.setCheckOut(DateTimeConverter.toLocalDateTime(req.getCheckOut()));
        return entity;
    }

    public void update(AccommodationRequest req) {
        this.setName(req.getAccommodationName());
        this.setAccommodationAddress(KakaoMapApi.getAddressByKeyword(req.getAccommodationName()));
        this.setCheckIn(DateTimeConverter.toLocalDateTime(req.getCheckIn()));
        this.setCheckOut(DateTimeConverter.toLocalDateTime(req.getCheckOut()));
    }
}
