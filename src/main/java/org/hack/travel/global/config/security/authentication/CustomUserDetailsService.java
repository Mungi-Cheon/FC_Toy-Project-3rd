package org.hack.travel.global.config.security.authentication;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.exception.UserException;
import org.hack.travel.domain.user.entity.UserEntity;
import org.hack.travel.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userData = userRepository.findByEmail(email);

        if (userData.isPresent()) {
            return new CustomUserDetails(userData.get());
        }
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(
                () -> new UserException(MessageType.USER_NOT_FOUND, email)
            );
        return new CustomUserDetails(userEntity);
    }
}

