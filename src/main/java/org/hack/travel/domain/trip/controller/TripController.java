package org.hack.travel.domain.trip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.response.ResponseResult;
import org.hack.travel.domain.trip.dto.request.TripRequest;
import org.hack.travel.domain.trip.dto.response.TripResponse;
import org.hack.travel.domain.trip.dto.response.TripSimpleResponse;
import org.hack.travel.domain.trip.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trips")
@Tag(name = "Trip", description = "여행 API")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping()
    @Operation(summary = "여행 등록 API", description = "여행 정보를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "여행 등록 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponse.class)))
    public ResponseEntity<ResponseResult> createTrip(
        Authentication authentication,
        @RequestBody TripRequest tripRequest) {
        var response = ResponseResult.created(MessageType.CREATED, MessageAffixType.TRIP,
            tripService.save(authentication.getName(), tripRequest));
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping()
    @Operation(summary = "여행 전체 조회 API ", description = "여행 전체 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "여행 전체 조회",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripSimpleResponse.class)))
    public ResponseEntity<ResponseResult> findAllTrips() {
        var response = ResponseResult.success(MessageType.READ_ALL, MessageAffixType.TRIP,
            tripService.findAllTrips());
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/{trip-id}")
    @Operation(summary = "여행 개별 조회 API", description = "선택한 번호의 여행 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "여행 개별 조회 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponse.class)))
    public ResponseEntity<ResponseResult> findTrip(
        @PathVariable("trip-id") int tripId) {
        var response = ResponseResult.success(MessageType.READ, MessageAffixType.TRIP,
            tripService.findTripById(tripId));
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @PutMapping("/{trip-id}")
    @Operation(summary = "여행 수정 API", description = "선택한 번호의 여행 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "여행 수정 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponse.class)))
    public ResponseEntity<ResponseResult> updateTrip(
        Authentication authentication,
        @RequestBody TripRequest tripRequest,
        @PathVariable("trip-id") int tripId) {
        var response = ResponseResult.success(MessageType.UPDATE, MessageAffixType.TRIP,
            tripService.updateTripById(authentication.getName(), tripRequest, tripId));
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @DeleteMapping("/{trip-id}")
    @Operation(summary = "여행 삭제 API", description = "선택한 번호의 여행 정보를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "여행 삭제 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponse.class)))
    public ResponseEntity<ResponseResult> deleteTrip(
        Authentication authentication,
        @PathVariable("trip-id") int tripId) {
        tripService.deleteTripById(authentication.getName(), tripId);
        var response = ResponseResult.success(MessageType.DELETE, MessageAffixType.TRIP, tripId);
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/search")
    @Operation(summary = "여행 검색 API", description = "키워드가 포함된 여행 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "여행 조회 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponse.class)))
    public ResponseEntity<ResponseResult> findTripByKeyword(
        @RequestParam String keyword) {
        List<TripResponse> tripList = tripService.findTripsByKeyword(keyword);
        if (tripList.isEmpty()) {
            var response = ResponseResult.error(MessageType.TRIP_KEYWORD_NOT_FOUND, keyword);
            return ResponseEntity.status(response.getResultCode()).body(response);
        } else {
            var response = ResponseResult.success(MessageType.TRIP_KEYWORD_OK_READ, keyword, tripList);
            return ResponseEntity.status(response.getResultCode()).body(response);
        }
    }
}
