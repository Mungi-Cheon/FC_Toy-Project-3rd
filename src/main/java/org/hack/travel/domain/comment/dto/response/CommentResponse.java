package org.hack.travel.domain.comment.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hack.travel.domain.comment.entity.CommentEntity;

@Getter
@AllArgsConstructor
@Schema(description = "댓글 Response")
public class CommentResponse {

    @Schema(description = "댓글 id")
    private Integer id;

    @Schema(description = "여행 id")
    private Integer tripId;

    @Schema(description = "회원 id")
    private Integer userId;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "댓글 작성 일자")
    private LocalDateTime createdAt;

    @Schema(description = "댓글 수정 일자")
    private LocalDateTime updatedAt;

    public static CommentResponse fromEntity(CommentEntity entity) {
        return new CommentResponse(
            entity.getId(),
            entity.getTripEntity().getId(),
            entity.getUserEntity().getId(),
            entity.getContent(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
