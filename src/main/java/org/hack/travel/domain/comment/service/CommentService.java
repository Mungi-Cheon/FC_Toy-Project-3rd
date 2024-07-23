package org.hack.travel.domain.comment.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hack.travel.domain.comment.dto.request.CommentRequest;
import org.hack.travel.domain.comment.dto.response.CommentResponse;
import org.hack.travel.domain.comment.entity.CommentEntity;
import org.hack.travel.domain.comment.repository.CommentRepository;
import org.hack.travel.domain.trip.entity.TripEntity;
import org.hack.travel.domain.trip.repository.TripRepository;
import org.hack.travel.domain.user.entity.UserEntity;
import org.hack.travel.domain.user.repository.UserRepository;
import org.hack.travel.global.exception.CommentException;
import org.hack.travel.global.exception.TripException;
import org.hack.travel.global.exception.UserException;
import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.util.CommentValidationUtil;
import org.hack.travel.global.util.UserValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    @Transactional
    public CommentResponse createComment(Integer tripId, CommentRequest commentRequest, String username) {
        CommentValidationUtil.validateComment(commentRequest);

        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(
            () -> new UserException(MessageType.USER_NOT_FOUND)
        );

        TripEntity tripEntity = tripRepository.findById(tripId).orElseThrow(
            () -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP)
        );

        CommentEntity entity = commentRepository.save(
            CommentEntity.of(commentRequest, tripEntity, userEntity));
        return CommentResponse.fromEntity(entity);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getAllComments(Integer tripId, String username) {
        userRepository.findByEmail(username).orElseThrow(
            () -> new UserException(MessageType.USER_NOT_FOUND)
        );

        tripRepository.findById(tripId).orElseThrow(
            () -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP)
        );

        return commentRepository.findAll().stream()
            .map(CommentResponse::fromEntity)
            .toList();
    }

    @Transactional
    public CommentResponse updateComment(Integer tripId, Integer commentId, CommentRequest commentRequest, String username) {
        CommentValidationUtil.validateComment(commentRequest);

        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(
            () -> new UserException(MessageType.USER_NOT_FOUND)
        );

        tripRepository.findById(tripId).orElseThrow(
            () -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP)
        );

        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(
            () -> new CommentException(MessageType.ID_NOT_FOUND, MessageAffixType.COMMENT)
        );

        if (!UserValidationUtil.isEqualUser(userEntity.getId(), commentEntity.getUserEntity().getId())) {
            throw new UserException(MessageType.NOT_EQUAL_USER);
        }

        CommentEntity updatedComment = commentEntity.update(commentRequest);
        return CommentResponse.fromEntity(commentRepository.save(updatedComment));
    }

    @Transactional
    public void deleteComment(Integer tripId, Integer commentId, String username) {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(
            () -> new UserException(MessageType.USER_NOT_FOUND)
        );

        tripRepository.findById(tripId).orElseThrow(
            () -> new TripException(MessageType.ID_NOT_FOUND, MessageAffixType.TRIP)
        );

        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(
            () -> new CommentException(MessageType.ID_NOT_FOUND, MessageAffixType.COMMENT)
        );

        if (!UserValidationUtil.isEqualUser(userEntity.getId(), commentEntity.getUserEntity().getId())) {
            throw new UserException(MessageType.NOT_EQUAL_USER);
        }

        commentRepository.delete(commentEntity);
    }
}
