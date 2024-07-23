package org.hack.travel.global.handler;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hack.travel.global.response.ResponseResult;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@RestControllerAdvice(basePackages = "org.hack.travel.domain")
@Order(value = 1)
public class TravelApiExceptionHandler {

    @ExceptionHandler(value = {HttpStatusCodeException.class})
    public ResponseEntity<ResponseResult> noSuchException(HttpStatusCodeException e) {
        var response = ResponseResult.builder()
            .resultCode(e.getStatusCode().value())
            .resultMessages(List.of(e.getMessage()))
            .build();
        return ResponseEntity.status(e.getStatusCode()).body(response);
    }
}
