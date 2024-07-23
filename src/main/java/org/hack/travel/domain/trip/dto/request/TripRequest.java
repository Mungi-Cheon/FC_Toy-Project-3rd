package org.hack.travel.domain.trip.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "여행 요청 DTO")
@Getter
public class TripRequest {

    @Schema(description = "여행 이름", example = "국내 여행 3박")
    private String name;

    @Schema(description = "여행 시작 날짜", example = "2024-05-01")
    private String startDate;

    @Schema(description = "여행 종료 날짜", example = "2024-05-04")
    private String endDate;

    @Schema(description = "국내/외 여행 여부", example = "true / false")
    private boolean domestic;

}