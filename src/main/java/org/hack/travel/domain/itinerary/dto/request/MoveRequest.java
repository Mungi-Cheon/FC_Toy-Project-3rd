package org.hack.travel.domain.itinerary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MoveRequest {

    @Schema(description = "출발지")
    private String departurePlace;

    @Schema(description = "도착지")
    private String destination;

    @Schema(description = "출발 시작 시간")
    private String departureTime;

    @Schema(description = "도착 시작 시간")
    private String arrivalTime;

    @Schema(description = "이동 수단")
    private String transportation;
}
