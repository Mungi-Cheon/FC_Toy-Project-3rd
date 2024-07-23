package org.hack.travel.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class TravelDateTimeException extends HttpStatusCodeException {

    public TravelDateTimeException(String value, String format) {
        super(HttpStatus.BAD_REQUEST,
            String.format("{ %s } 은 잘못된 시간 형식입니다. (올바른 예시:%s)", value, format));
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}
