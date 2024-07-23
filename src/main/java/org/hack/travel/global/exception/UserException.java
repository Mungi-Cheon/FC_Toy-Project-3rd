package org.hack.travel.global.exception;

import org.hack.travel.global.exception.type.MessageType;
import org.springframework.web.client.HttpStatusCodeException;

public class UserException extends HttpStatusCodeException {

    public UserException() {
        super(MessageType.NOT_EQUAL_USER.getStatus(), MessageType.NOT_EQUAL_USER.getMessage());
    }

    public UserException(MessageType type) {
        super(type.getStatus(), type.getMessage());
    }

    public UserException(MessageType type, String userName) {
        super(type.getStatus(), String.format(type.getMessage(), userName));
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}
