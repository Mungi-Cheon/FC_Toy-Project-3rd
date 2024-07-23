package org.hack.travel.domain.itinerary.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hack.travel.domain.itinerary.entity.MoveEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "이동 Response")
public class MoveResponse {

    @Schema(description = "출발지")
    private String departurePlace;

    @Schema(description = "출발지 주소")
    private String departurePlaceRoadAddressName;

    @Schema(description = "도착지")
    private String destination;
    
    @Schema(description = "도착지 주소")
    private String destinationRoadAddressName;

    @Schema(description = "출발 시간")
    private LocalDateTime departureTime;

    @Schema(description = "도착 시간")
    private LocalDateTime arrivalTime;

    @Schema(description = "이동 수단")
    private String transportation;

    public static MoveResponse fromEntity(MoveEntity entity) {
        return new MoveResponse(
            entity.getDeparturePlace(),
            entity.getDestination(),
            entity.getDeparturePlaceAddress(),
            entity.getDestinationAddress(),
            entity.getDepartureTime(),
            entity.getArrivalTime(),
            entity.getTransportation()
        );
    }
}
