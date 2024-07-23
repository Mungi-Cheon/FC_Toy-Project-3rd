package org.hack.travel.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageAffixType {

    TRIP("여행 정보"),
    TRIP_ALL("여행 정보 리스트"),
    ITINERARY("여정 정보"),
    STAY_START("체류 시작 일시"),
    STAY_END("체류 종료 일시"),
    MOVE_START("이동 시작 일시"),
    MOVE_END("이동 종료 일시"),
    CHECK_IN("체크인"),
    CHECK_OUT("체크아웃"),
    COMMENT("댓글"),
    COMMENT_ALL("댓글 리스트"),
    USER("회원"),
    ADDRESS("주소");

    private final String type;
}
