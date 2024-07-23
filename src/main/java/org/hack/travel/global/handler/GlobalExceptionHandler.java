package org.hack.travel.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import java.util.List;
import org.hack.travel.global.response.ResponseResult;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseResult> exceptionHandle(HttpServletRequest req, Exception e) {
        var response = ResponseResult.builder()
            .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .resultMessages(List.of(e.getMessage()))
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}
