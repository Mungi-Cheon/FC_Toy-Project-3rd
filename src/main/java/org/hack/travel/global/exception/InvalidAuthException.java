package org.hack.travel.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.HttpStatusCodeException;

public class InvalidAuthException extends HttpStatusCodeException {

    public InvalidAuthException(AuthenticationException ex) {
        super(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}
