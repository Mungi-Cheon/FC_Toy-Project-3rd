package org.hack.travel.global.exception;

import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.springframework.web.client.HttpStatusCodeException;

public class TripException extends HttpStatusCodeException {

    public TripException(MessageType type, int tripId) {
        super(type.getStatus(), String.format(type.getMessage(), tripId));
    }

    public TripException(MessageType type, MessageAffixType affixType) {
        super(type.getStatus(), String.format(type.getMessage(), affixType.getType()));
    }

    public TripException(MessageType type, String keyword) {
        super(type.getStatus(), String.format(type.getMessage(), keyword));
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}
