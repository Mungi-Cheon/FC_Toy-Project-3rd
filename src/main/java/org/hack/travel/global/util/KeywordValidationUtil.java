package org.hack.travel.global.util;

import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.exception.TripException;

public class KeywordValidationUtil {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;

    public static void validateKeyword(String keyword) {
        if (keyword.isBlank()) {
            throw new TripException(MessageType.TRIP_KEYWORD_BAD_REQUEST, keyword);
        }

        if (!(MIN_LENGTH <= keyword.length() && keyword.length() <= MAX_LENGTH)) {
            throw new TripException(MessageType.TRIP_KEYWORD_BAD_REQUEST, keyword);
        }
    }
}