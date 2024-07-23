package org.hack.travel.domain.itinerary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;

@Getter
public class ItineraryRequest {

    @Schema(description = "여행 ID")
    private int tripId;

    @Schema(description = "여정 이름")
    private String name;

    @Valid
    @Schema(description = "체류 계획")
    private StayRequest stay;

    @Valid
    @Schema(description = "이동 계획")
    private MoveRequest move;

    @Valid
    @Schema(description = "숙박 계획")
    private AccommodationRequest accommodation;
}
