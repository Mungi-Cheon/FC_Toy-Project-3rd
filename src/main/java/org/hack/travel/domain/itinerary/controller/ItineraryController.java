package org.hack.travel.domain.itinerary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hack.travel.domain.itinerary.dto.request.ItineraryRequest;
import org.hack.travel.domain.itinerary.dto.response.ItineraryResponse;
import org.hack.travel.domain.itinerary.service.ItineraryService;
import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.response.ResponseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Itinerary", description = "여정 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;

    @PostMapping("/itineraries")
    @Operation(summary = "여정 등록 API", description = "여정 정보를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "여정 등록 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItineraryResponse.class))
        )
    public ResponseEntity<ResponseResult> createItinerary(
        @RequestBody ItineraryRequest itineraryRequest,
        Authentication authentication
    ) {
        var response = ResponseResult.created(MessageType.CREATED, MessageAffixType.ITINERARY,
            itineraryService.createItinerary(itineraryRequest, authentication.getName()));
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/trips/{trip-id}/itineraries/{itinerary-id}")
    @Operation(summary = "여정 조회 API", description = "특정 여행에 속한 특정 여정을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "여정 조회 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItineraryResponse.class))
    )
    public ResponseEntity<ResponseResult> findItineraryByTripId(
        @PathVariable("trip-id") int tripId,
        @PathVariable("itinerary-id") int itineraryId
    ) {
        var response = ResponseResult.success(MessageType.READ, MessageAffixType.ITINERARY,
            itineraryService.findItineraryByTripId(tripId, itineraryId));
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @PutMapping("/trips/{trip-id}/itineraries/{itinerary-id}")
    @Operation(summary = "여정 수정 API", description = "선택한 번호의 여정 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "여정 수정 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItineraryResponse.class)))
    public ResponseEntity<ResponseResult> updateItinerary(
        @PathVariable("trip-id") int tripId,
        @PathVariable("itinerary-id") int itineraryId,
        @RequestBody ItineraryRequest updatedRequest,
        Authentication authentication) {

        var response = ResponseResult.success(MessageType.UPDATE, MessageAffixType.ITINERARY,
            itineraryService.updateItinerary(tripId, itineraryId, updatedRequest, authentication.getName()));
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @DeleteMapping("/trips/{trip-id}/itineraries/{itinerary-id}")
    @Operation(summary = "여정 삭제 API", description = "선택한 번호의 여정 정보를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "여정 삭제 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItineraryResponse.class)))
    public ResponseEntity<ResponseResult> deleteItinerary(
        @PathVariable("trip-id") int tripId,
        @PathVariable("itinerary-id") int itineraryId,
        Authentication authentication) {
        itineraryService.deleteItinerary(tripId, itineraryId, authentication.getName());
        var response = ResponseResult.success(MessageType.DELETE, MessageAffixType.ITINERARY,
            itineraryId);
        return ResponseEntity.status(response.getResultCode()).body(response);
    }
}
