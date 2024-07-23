package org.hack.travel.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum MessageType {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보가 없습니다."),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),
    CREATED(HttpStatus.CREATED, "%s을(를) 등록했습니다"),
    READ_ALL(HttpStatus.OK, "%s을(를) 조회했습니다."),
    READ(HttpStatus.OK, "%s을(를) 조회했습니다."),
    UPDATE(HttpStatus.OK, "%s을(를) 수정했습니다."),
    DELETE(HttpStatus.OK, "%s을(를) 삭제했습니다."),
    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "입력한 ID의 %s이(가) 없습니다."),
    NOT_EQUAL_USER(HttpStatus.FORBIDDEN, "등록한 회원만 변경 및 삭제할 수 있습니다."),
    TRIP_KEYWORD_OK_READ(HttpStatus.OK, "입력한 검색어 [ %s ] 관련 여행 정보를 조회했습니다."),
    TRIP_KEYWORD_NOT_FOUND(HttpStatus.OK, "입력한 검색어 [ %s ] 관련 여행 정보가 없습니다."),
    TRIP_KEYWORD_BAD_REQUEST(HttpStatus.BAD_REQUEST, "검색어는 공백이 아니며, 2자 이상 100 이하의 한글만 입력 가능합니다."),
    MUST_BE_AFTER(HttpStatus.BAD_REQUEST, "%s는 %s 이후여야 합니다."),
    ITINERARY_DATE_MUST_BE_BETWEEN_START_AND_END(HttpStatus.BAD_REQUEST,
        "여정 일시는 여행 시작 일시와 종료 일시 사이에 포함되어야 합니다."),
    TRIP_CREATION_RESTRICTION_MESSAGE(HttpStatus.BAD_REQUEST, "여정 정보의 등록은 여행 정보를 등록한 회원만 가능합니다."),
    COMMENT_EMPTY(HttpStatus.NOT_FOUND, "댓글 내용이 비어있습니다."),
    LIKE_CREATED(HttpStatus.CREATED, "여행 정보에 좋아요를 눌렀습니다."),
    LIKE_OK_READ_LIKED_ALL_TRIP(HttpStatus.OK, "좋아요를 누른 여행 정보 리스트를 조회합니다."),
    LIKE_OK_READ(HttpStatus.OK, "{ %d }번 여행 정보의 좋아요 개수를 조회합니다."),
    LIKE_OK_CANCEL(HttpStatus.OK, "현재 여행 정보의 좋아요를 취소했습니다."),
    LIKE_ERR_ALREADY_EXIST_LIKE(HttpStatus.CONFLICT, "이미 좋아요를 눌렀습니다."),
    LIKE_ERR_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 좋아요는 이미 취소되었거나 존재하지 않습니다."),
    ADDRESS_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "입력한 주소 [ %s ] 의 정보가 없습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인했습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃했습니다."),
    REPUBLISH_TOKEN(HttpStatus.OK, "토큰을 재발행했습니다."),
    REFRESH_TOKEN_IS_NULL(HttpStatus.UNAUTHORIZED, "리플래쉬 토큰이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리플래쉬 토큰이 유효하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리플래쉬 토큰의 기간이 만료되었습니다.");
    private final HttpStatus status;
    private final String message;

}