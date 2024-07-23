package org.hack.travel.global.util;

import org.hack.travel.global.exception.CommentException;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.domain.comment.dto.request.CommentRequest;

public class CommentValidationUtil {

    public static void validateComment(CommentRequest commentRequest) {
        if (commentRequest.getContent().isBlank()) {
            throw new CommentException(MessageType.COMMENT_EMPTY);
        }
    }
}