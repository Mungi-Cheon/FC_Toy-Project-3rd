package org.hack.travel.global.exception;

import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.springframework.web.client.HttpStatusCodeException;

public class ItineraryException extends HttpStatusCodeException {

    public ItineraryException(MessageType type) {
        super(type.getStatus(), type.getMessage());
    }

    public ItineraryException(MessageType type, MessageAffixType affixType) {
        super(type.getStatus(), String.format(type.getMessage(), affixType.getType()));
    }

    public ItineraryException(MessageAffixType prefix, MessageType msgType, MessageAffixType suffix) {
        super(msgType.getStatus(),String.format(msgType.getMessage(), prefix.getType(), suffix.getType()));
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}
