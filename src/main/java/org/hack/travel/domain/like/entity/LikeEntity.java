package org.hack.travel.domain.like.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hack.travel.domain.trip.entity.TripEntity;
import org.hack.travel.domain.user.entity.UserEntity;

@Setter(value = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "likes")
@Schema(description = "좋아요")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonIgnore
    private TripEntity tripEntity;

    public static LikeEntity of(TripEntity tripEntity, UserEntity userEntity) {
        LikeEntity entity = new LikeEntity();
        entity.setTripEntity(tripEntity);
        entity.setUserEntity(userEntity);
        return entity;
    }
}
