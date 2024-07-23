package org.hack.travel.domain.itinerary.repository;

import org.hack.travel.domain.itinerary.entity.ItineraryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<ItineraryEntity, Integer> {

}
