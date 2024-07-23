package org.hack.travel.domain.itinerary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AccommodationRequest {

    @Schema(description = "숙박 장소")
    private String accommodationName;

    @Schema(description = "숙박 시작 시간")
    private String checkIn;

    @Schema(description = "숙박 종료 시간")
    private String checkOut;
}
