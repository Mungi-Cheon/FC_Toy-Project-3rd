package org.hack.travel.domain.user.service;

import lombok.AllArgsConstructor;
import org.hack.travel.domain.user.dto.request.UserRequest;
import org.hack.travel.domain.user.dto.response.UserResponse;
import org.hack.travel.domain.user.entity.UserEntity;
import org.hack.travel.domain.user.repository.UserRepository;
import org.hack.travel.global.exception.UserException;
import org.hack.travel.global.exception.type.MessageType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserResponse join(UserRequest request) {
        String email = request.getEmail();
        Boolean isExist = userRepository.existsByEmail(email);
        if (isExist) {
            throw new UserException(MessageType.USER_ALREADY_EXIST);
        }
        return UserResponse.fromEntity(
            userRepository.save(UserEntity.of(request, bCryptPasswordEncoder)));
    }
}