package org.hack.travel.domain.user.repository;

import java.util.Optional;
import org.hack.travel.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
