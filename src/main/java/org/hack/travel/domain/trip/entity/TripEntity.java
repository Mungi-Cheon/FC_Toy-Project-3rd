package org.hack.travel.domain.trip.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hack.travel.global.converter.DateTimeConverter;
import org.hack.travel.domain.comment.entity.CommentEntity;
import org.hack.travel.domain.itinerary.entity.ItineraryEntity;
import org.hack.travel.domain.like.entity.LikeEntity;
import org.hack.travel.domain.trip.dto.request.TripRequest;
import org.hack.travel.domain.user.entity.UserEntity;

@Setter(value = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "trip")
public class TripEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "is_domestic")
    private boolean domestic;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity userEntity;

    @OneToMany(mappedBy = "tripEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ItineraryEntity> itineraries = new ArrayList<>();

    @OneToMany(mappedBy = "tripEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "tripEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<LikeEntity> likes = new ArrayList<>();

    @PrePersist
    void createdAt() {
        this.createdAt = DateTimeConverter.now();
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = DateTimeConverter.now();
    }

    @Builder
    public TripEntity(int id, String name, LocalDate startDate, LocalDate endDate, boolean domestic){
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.domestic = domestic;
    }

    public static TripEntity of(TripRequest req, UserEntity userEntity) {
        TripEntity entity = new TripEntity();
        entity.setName(req.getName());
        entity.setStartDate(DateTimeConverter.toLocalDate(req.getStartDate()));
        entity.setEndDate(DateTimeConverter.toLocalDate(req.getEndDate()));
        entity.setDomestic(req.isDomestic());
        entity.setUserEntity(userEntity);
        return entity;
    }

    public TripEntity update(TripRequest req, UserEntity userEntity) {
        this.name = req.getName();
        this.startDate = DateTimeConverter.toLocalDate(req.getStartDate());
        this.endDate = DateTimeConverter.toLocalDate(req.getEndDate());
        this.domestic = req.isDomestic();
        this.userEntity = userEntity;
        return this;
    }
}
