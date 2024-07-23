package org.hack.travel.global.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hack.travel.global.exception.type.MessageAffixType;
import org.hack.travel.global.exception.type.MessageType;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

@Getter
@Builder
@AllArgsConstructor
public class ResponseResult<T> {

    private Integer resultCode;
    private List<String> resultMessages;
    private T data;

    public static <T> ResponseResult<T> success(MessageType type, T data) {
        return success(type.getStatus().value(), type.getMessage(), data);
    }

    public static <T> ResponseResult<T> success(MessageType type, int id, T data) {
        return success(type.getStatus().value(), String.format(type.getMessage(), id), data);
    }

    public static <T> ResponseResult<T> success(MessageType type, MessageAffixType affixType,
        T data) {
        return success(type.getStatus().value(),
            String.format(type.getMessage(), affixType.getType()), data);
    }

    public static <T> ResponseResult<T> success(MessageType type, String message, T data) {
        return success(type.getStatus().value(), String.format(type.getMessage(), message), data);
    }

    private static <T> ResponseResult<T> success(int status, String message, T data) {
        return ResponseResult.<T>builder().resultCode(status).resultMessages(List.of(message))
            .data(data).build();
    }

    public static <T> ResponseResult<T> created(MessageType type, T data) {
        return ResponseResult.<T>builder().resultCode(type.getStatus().value())
            .resultMessages(List.of(type.getMessage()))
            .data(data).build();
    }

    public static <T> ResponseResult<T> created(MessageType type, MessageAffixType affixType, T data) {
        return ResponseResult.<T>builder().resultCode(type.getStatus().value())
            .resultMessages(List.of(String.format(type.getMessage(), affixType.getType())))
            .data(data).build();
    }

    public static <T> ResponseResult<T> error(HttpStatusCodeException ex) {
        return ResponseResult.<T>builder().resultCode(ex.getStatusCode().value())
            .resultMessages(List.of(ex.getMessage()))
            .build();
    }
    public static <T> ResponseResult<T> error(MessageType type, String message) {
        return ResponseResult.<T>builder().resultCode(type.getStatus().value())
            .resultMessages(List.of(String.format(type.getMessage(), message)))
            .build();
    }
}
