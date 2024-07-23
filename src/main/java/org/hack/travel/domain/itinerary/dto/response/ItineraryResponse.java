package org.hack.travel.domain.itinerary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hack.travel.domain.itinerary.entity.ItineraryEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "여정 Response")
public class ItineraryResponse {

    @Schema(description = "여정 ID")
    private Integer id;

    @Schema(description = "여정 이름")
    private String name;

    @Schema(description = "이동 계획")
    private MoveResponse moveResponse;

    @Schema(description = "체류 계획")
    private StayResponse stayResponse;

    @Schema(description = "숙박 계획")
    private AccommodationResponse accommodationResponse;

    @Schema(description = "등록 일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시")
    private LocalDateTime updatedAt;

    public static ItineraryResponse fromEntity(ItineraryEntity entity) {
        return new ItineraryResponse(entity.getId(), entity.getName(),
            MoveResponse.fromEntity(entity.getMoveEntity()),
            StayResponse.fromEntity(entity.getStayEntity()),
            AccommodationResponse.fromEntity(entity.getAccommodationEntity()),
            entity.getCreatedAt(), entity.getUpdatedAt());
    }
}