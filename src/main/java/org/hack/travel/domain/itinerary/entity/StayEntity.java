package org.hack.travel.domain.itinerary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hack.travel.domain.itinerary.api.KakaoMapApi;
import org.hack.travel.domain.itinerary.dto.request.StayRequest;
import org.hack.travel.global.converter.DateTimeConverter;


@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PRIVATE)
public class StayEntity {

    private String place;

    @Column(name = "stay_road_address_name")
    private String stayAddress;

    @Column(name = "stay_start_time")
    private LocalDateTime startTime;

    @Column(name = "stay_end_time")
    private LocalDateTime endTime;

    public static StayEntity of(StayRequest req) {
        StayEntity entity = new StayEntity();
        entity.setPlace(req.getPlace());
        entity.setStayAddress(KakaoMapApi.getAddressByKeyword(req.getPlace()));
        entity.setStartTime(DateTimeConverter.toLocalDateTime(req.getStartTime()));
        entity.setEndTime(DateTimeConverter.toLocalDateTime(req.getEndTime()));
        return entity;
    }

    public void update(StayRequest req) {
        this.setPlace(req.getPlace());
        this.setStayAddress(KakaoMapApi.getAddressByKeyword(req.getPlace()));
        this.setStartTime(DateTimeConverter.toLocalDateTime(req.getStartTime()));
        this.setEndTime(DateTimeConverter.toLocalDateTime(req.getEndTime()));
    }
}
