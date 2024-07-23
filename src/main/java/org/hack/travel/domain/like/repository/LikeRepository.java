package org.hack.travel.domain.like.repository;

import java.util.Optional;
import org.hack.travel.domain.like.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {

    @Query(value = "SELECT COUNT(*) from likes l WHERE l.tripEntity.id = :tripId")
    Integer findLikeCountByTripId(@Param("tripId") Integer tripId);

    @Query(value = "SELECT l FROM likes l WHERE l.userEntity.id = :userId and l.tripEntity.id = :tripId")
    Optional<LikeEntity> findLikeEntityByUserIdAndTripId(@Param("userId") Integer userId,
        @Param("tripId") Integer tripId);

    void deleteLikeById(Integer id);
}
