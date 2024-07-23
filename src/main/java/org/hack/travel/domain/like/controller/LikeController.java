package org.hack.travel.domain.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.response.ResponseResult;
import org.hack.travel.domain.like.entity.LikeEntity;
import org.hack.travel.domain.like.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Like", description = "좋아요 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/trips/{trip-id}/likes")
    @Operation(summary = "좋아요 등록 API", description = "좋아요를 누릅니다.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "좋아요 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = LikeEntity.class)))
    )
    public ResponseEntity<ResponseResult> like(
        @PathVariable("trip-id") Integer tripId,
        Authentication authentication) {
        likeService.like(tripId, authentication.getName());
        var response = ResponseResult.success(MessageType.LIKE_CREATED, tripId, null);
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/trips/{trip-id}/likes")
    @Operation(summary = "특정 여행 정보에 대한 좋아요 개수 조회 API", description = "특정 여행 정보의 좋아요 개수를 조회합니다.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "좋아요 개수 조회 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = LikeEntity.class)))
    )
    public ResponseEntity<ResponseResult> getLikes(
        @PathVariable("trip-id") Integer tripId) {
        int likeCount = likeService.getLikeCount(tripId);
        var response = ResponseResult.success(MessageType.LIKE_OK_READ, tripId, likeCount);
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/user/liked")
    @Operation(summary = "회원이 좋아요 누른 여행 정보 전체 조회 API", description = "좋아요를 누른 여행 정보 전체를 조회합니다.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "좋아요 누른 여행 정보 전체 조회 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = LikeEntity.class)))
    )
    public ResponseEntity<ResponseResult> getTripsByUserLiked(
        Authentication authentication) {
        var response = ResponseResult.success(MessageType.LIKE_OK_READ_LIKED_ALL_TRIP,
            likeService.findAllTripByLiked(authentication.getName()));
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @DeleteMapping("{trip-id}/likes")
    @Operation(summary = "좋아요 취소 API", description = "좋아요 취소를 누릅니다.")
    @ApiResponse(responseCode = "200", description = "좋아요 취소 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = LikeEntity.class)))
    public ResponseEntity<ResponseResult> cancelLike(
        @PathVariable("trip-id") Integer tripId,
        Authentication authentication) {
        likeService.deleteLike(tripId, authentication.getName());
        var response = ResponseResult.success(MessageType.LIKE_OK_CANCEL, tripId, null);
        return ResponseEntity.status(response.getResultCode()).body(response);
    }
}
