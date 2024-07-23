package org.hack.travel.domain.trip.repository;

import java.util.List;
import java.util.Optional;
import org.hack.travel.domain.trip.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TripRepository extends JpaRepository<TripEntity, Integer> {

    Optional<TripEntity> findByIdAndUserEntityId(Integer tripId, Integer userId);

    void deleteByIdAndUserEntityId(Integer tripId, Integer userId);

    List<TripEntity> findByNameContains(String name);

    @Query("SELECT t, COUNT(l.id) as like_count FROM trip t LEFT JOIN likes l ON t.id = l.tripEntity.id GROUP BY t.id")
    List<TripEntity> findAllTripJoinLike();
}
