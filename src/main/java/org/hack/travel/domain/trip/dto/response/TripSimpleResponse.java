package org.hack.travel.domain.trip.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hack.travel.domain.trip.entity.TripEntity;

@Getter
@AllArgsConstructor
@Schema(description = "여행 전체 조회 Response")
public class TripSimpleResponse {

    @Schema(description = "여행 ID")
    private Integer id;

    @Schema(description = "유저 ID")
    private Integer userId;

    @Schema(description = "여행 이름")
    private String name;

    @Schema(description = "여행 시작 일시")
    private LocalDate startDate;

    @Schema(description = "여행 종료 일시")
    private LocalDate endDate;

    @Schema(description = "국내/외")
    private boolean domestic;

    @Schema(description = "좋아요 수")
    private Integer likeCnt;

    @Schema(description = "등록 일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시")
    private LocalDateTime updatedAt;

    public static TripSimpleResponse fromEntity(TripEntity entity) {
        return new TripSimpleResponse(
            entity.getId(),
            entity.getUserEntity().getId(),
            entity.getName(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.isDomestic(),
            entity.getLikes().size(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
