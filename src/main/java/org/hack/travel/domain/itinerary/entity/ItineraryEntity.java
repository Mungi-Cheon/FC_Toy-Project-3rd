package org.hack.travel.domain.itinerary.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hack.travel.domain.itinerary.dto.request.ItineraryRequest;
import org.hack.travel.domain.trip.entity.TripEntity;
import org.hack.travel.global.converter.DateTimeConverter;

@Entity(name = "itinerary")
@Schema(description = "여정")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PRIVATE)
public class ItineraryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private TripEntity tripEntity;

    @Embedded
    private StayEntity stayEntity;

    @Embedded
    private MoveEntity moveEntity;

    @Embedded
    private AccommodationEntity accommodationEntity;

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

    public static ItineraryEntity of(ItineraryRequest req, TripEntity tripEntity) {
        ItineraryEntity entity = new ItineraryEntity();
        entity.setName(req.getName());
        entity.setTripEntity(tripEntity);
        entity.setMoveEntity(MoveEntity.of(req.getMove()));
        entity.setStayEntity(StayEntity.of(req.getStay()));
        entity.setAccommodationEntity(AccommodationEntity.of(req.getAccommodation()));
        return entity;
    }

    public ItineraryEntity update(ItineraryRequest req, TripEntity tripEntity) {
        this.name = req.getName();
        this.tripEntity = tripEntity;
        this.moveEntity = MoveEntity.of(req.getMove());
        this.stayEntity = StayEntity.of(req.getStay());
        this.accommodationEntity.update(req.getAccommodation());
        return this;
    }
}


