package org.hack.travel.global.exception;

import org.hack.travel.global.exception.type.MessageType;
import org.springframework.web.client.HttpStatusCodeException;

public class LocationNotFoundException extends HttpStatusCodeException {

	public LocationNotFoundException(MessageType type, String name) {
		super(type.getStatus(), String.format(type.getMessage(), name));
	}

	@Override
	public String getMessage() {
		return getStatusText();
	}
}
