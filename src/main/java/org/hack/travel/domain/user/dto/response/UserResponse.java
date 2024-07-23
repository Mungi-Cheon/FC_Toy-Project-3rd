package org.hack.travel.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hack.travel.domain.user.entity.UserEntity;

@Getter
@AllArgsConstructor
@Schema(description = "유저 Response")
public class UserResponse {

    @Schema(description = "유저 식별 ID")
    private int id;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "이름")
    private String username;

    public static UserResponse fromEntity(UserEntity entity) {
        return new UserResponse(
            entity.getId(),
            entity.getEmail(),
            entity.getUsername());
    }
}
