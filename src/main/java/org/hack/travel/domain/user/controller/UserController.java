package org.hack.travel.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.hack.travel.domain.user.dto.request.UserRequest;
import org.hack.travel.domain.user.dto.response.UserResponse;
import org.hack.travel.domain.user.service.UserService;
import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.response.ResponseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Tag(name = "User", description = "회원 API")
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입 API", description = "회원 정보를 등록합니다.")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "화원 가입 성공",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))))
    public ResponseEntity<ResponseResult> joinProcess(
        @RequestBody UserRequest userRequest) {
        var response = ResponseResult.created(MessageType.CREATED, MessageAffixType.USER,
            userService.join(userRequest));
        return ResponseEntity.status(response.getResultCode()).body(response);
    }
}
