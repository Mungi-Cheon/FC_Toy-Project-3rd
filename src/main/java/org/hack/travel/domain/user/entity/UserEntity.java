package org.hack.travel.domain.user.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hack.travel.domain.like.entity.LikeEntity;
import org.hack.travel.domain.trip.entity.TripEntity;
import org.hack.travel.domain.user.dto.request.UserRequest;
import org.hack.travel.domain.user.dto.response.UserResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Setter(value = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "user")
@Entity(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    private String username;

    private String password;

    private String role;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<LikeEntity> likeList = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<TripEntity> tripList = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public UserEntity(int id, String email, String role){
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public static UserEntity of(UserRequest req, BCryptPasswordEncoder bCryptPasswordEncoder){
        UserEntity entity = new UserEntity();
        entity.setEmail(req.getEmail());
        entity.setUsername(req.getUsername());
        entity.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
        entity.setRole("ROLE_USER");
        return entity;
    }
}
