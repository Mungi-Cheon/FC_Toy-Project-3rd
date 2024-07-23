package org.hack.travel.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hack.travel.domain.comment.dto.request.CommentRequest;
import org.hack.travel.domain.comment.dto.response.CommentResponse;
import org.hack.travel.domain.comment.service.CommentService;
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

@Tag(name = "Comment", description = "댓글 API")
@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{trip-id}/comments")
    @Operation(summary = "댓글 등록 API", description = "댓글을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "댓글 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class)))
    public ResponseEntity<ResponseResult> createComment(
        @PathVariable(value = "trip-id") Integer tripId, @RequestBody CommentRequest commentRequest,
        Authentication authentication) {
        var response = ResponseResult.created(MessageType.CREATED, MessageAffixType.COMMENT,
            commentService.createComment(tripId, commentRequest, authentication.getName()));
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/{trip-id}/comments")
    @Operation(summary = "댓글 조회 API ", description = "선택한 여행의 댓글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class)))
    public ResponseEntity<ResponseResult> getAllComments(
        @PathVariable(value = "trip-id") Integer tripId, Authentication authentication) {
        var response = ResponseResult.success(MessageType.READ_ALL, MessageAffixType.COMMENT_ALL,
            commentService.getAllComments(tripId, authentication.getName()));
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @PutMapping("/{trip-id}/comments/{comment-id}")
    @Operation(summary = "댓글 수정 API", description = "선택한 댓글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class)))
    public ResponseEntity<ResponseResult> updateComment(
        @PathVariable(value = "trip-id") Integer tripId,
        @PathVariable(value = "comment-id") Integer commentId,
        @RequestBody CommentRequest commentRequest, Authentication authentication) {
        var response = ResponseResult.success(MessageType.UPDATE, MessageAffixType.COMMENT,
            commentService.updateComment(tripId, commentId, commentRequest,
                authentication.getName()));
        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @DeleteMapping("/{trip-id}/comments/{comment-id}")
    @Operation(summary = "댓글 삭제", description = "선택한 댓글을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class)))
    public ResponseEntity<ResponseResult> deleteComment(
        @PathVariable(value = "trip-id") Integer tripId,
        @PathVariable(value = "comment-id") Integer commentId, Authentication authentication) {
        commentService.deleteComment(tripId, commentId, authentication.getName());
        var response = ResponseResult.success(MessageType.DELETE, MessageAffixType.COMMENT, null);
        return ResponseEntity.status(response.getResultCode()).body(response);
    }
}
