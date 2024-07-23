package org.hack.travel.domain.itinerary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hack.travel.domain.itinerary.entity.AccommodationEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "숙박 Response")
public class AccommodationResponse {

    @Schema(description = "숙박 장소명")
    private String name;

    @Schema(description = "숙박 장소 주소")
    private String accommodationAddress;

    @Schema(description = "체크인")
    private LocalDateTime checkIn;

    @Schema(description = "체크아웃")
    private LocalDateTime checkOut;

    public static AccommodationResponse fromEntity(AccommodationEntity entity) {
        return new AccommodationResponse(entity.getName(), entity.getAccommodationAddress(),
            entity.getCheckIn(), entity.getCheckOut());
    }
}
