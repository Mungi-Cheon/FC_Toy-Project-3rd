package org.hack.travel.global.exception;

import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.springframework.web.client.HttpStatusCodeException;

public class CommentException extends HttpStatusCodeException {

    public CommentException(MessageType type) {
        super(type.getStatus(), type.getMessage());
    }
    public CommentException(MessageType type, MessageAffixType affixType) {
        super(type.getStatus(), String.format(type.getMessage(), affixType.getType()));
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}