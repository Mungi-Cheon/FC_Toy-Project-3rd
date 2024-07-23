package org.hack.travel.domain.itinerary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hack.travel.global.converter.DateTimeConverter;

@Getter
public class StayRequest {

    @Schema(description = "체류 장소")
    private String place;

    @Schema(description = "체류 시작 시간")
    private String startTime;

    @Schema(description = "체류 종료 시간")
    private String endTime;
}
