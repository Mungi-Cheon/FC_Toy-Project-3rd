package org.hack.travel.domain.itinerary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hack.travel.domain.itinerary.entity.StayEntity;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "체류 Response")
public class StayResponse {

    @Schema(description = "체류 장소")
    private String place;

    @Schema(description = "체류 주소")
    private String stayRoadAddressName;

    @Schema(description = "체류 시작 시간")
    private LocalDateTime startTime;

    @Schema(description = "체류 종료 시간")
    private LocalDateTime endTime;

    public static StayResponse fromEntity(StayEntity entity) {
        return new StayResponse(
            entity.getPlace(),
            entity.getStayAddress(),
            entity.getStartTime(),
            entity.getEndTime());
    }
}
