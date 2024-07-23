package org.hack.travel.global.exception;

import org.hack.travel.global.exception.type.MessageType;
import org.springframework.web.client.HttpStatusCodeException;

public class LikeException extends HttpStatusCodeException {

    public LikeException(MessageType type) {
        super(type.getStatus(), type.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}
