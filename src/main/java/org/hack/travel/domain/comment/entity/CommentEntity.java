package org.hack.travel.domain.comment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hack.travel.global.converter.DateTimeConverter;
import org.hack.travel.domain.comment.dto.request.CommentRequest;
import org.hack.travel.domain.trip.entity.TripEntity;
import org.hack.travel.domain.user.entity.UserEntity;

@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "comment")
@Schema(description = "댓글")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonIgnore
    private TripEntity tripEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity userEntity;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = DateTimeConverter.now();
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = DateTimeConverter.now();
    }

    public static CommentEntity of(CommentRequest req, TripEntity tripEntity, UserEntity userEntity) {
        CommentEntity entity = new CommentEntity();
        entity.setContent(req.getContent());
        entity.setTripEntity(tripEntity);
        entity.setUserEntity(userEntity);
        return entity;
    }

    public CommentEntity update(CommentRequest req) {
        this.content = req.getContent();
        return this;
    }
}
