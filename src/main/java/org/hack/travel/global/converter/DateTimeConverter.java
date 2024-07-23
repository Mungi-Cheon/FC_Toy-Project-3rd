package org.hack.travel.global.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.hack.travel.global.exception.TravelDateTimeException;

public class DateTimeConverter {

    // regex
    public static final String REGEX_DATE = "(\\d{4})(-(\\d{2}))?(-(\\d{2}))";
    public static final String REGEX_DATE_TIME = REGEX_DATE
        + "?(\\s(\\d{2}):(\\d{2})(:(\\d{2}))?(\\.(\\d+))?(([\\+\\-]{1}\\d{2}:\\d{2})|Z)?)?";

    // pattern
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_TIME = PATTERN_DATE + " HH:mm:ss";

    public static LocalDate toLocalDate(String localDateStr) {
        try {
            return LocalDate.parse(localDateStr, DateTimeFormatter.ofPattern(PATTERN_DATE));
        } catch (DateTimeParseException e) {
            throw new TravelDateTimeException(localDateStr,PATTERN_DATE);
        }
    }
    public static LocalDateTime toLocalDateTime(String localDateTimeStr) {
        try {
            return LocalDateTime.parse(localDateTimeStr,
                DateTimeFormatter.ofPattern(PATTERN_DATE_TIME));
        } catch (DateTimeParseException e) {
            throw new TravelDateTimeException(localDateTimeStr,PATTERN_DATE_TIME);
        }
    }

    public static LocalDateTime now() {
        String formattedNow = "";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE_TIME);
            LocalDateTime now = LocalDateTime.now();
            formattedNow = now.format(formatter);
            return LocalDateTime.parse(formattedNow, formatter);
        } catch (DateTimeParseException e) {
            throw new TravelDateTimeException(formattedNow,PATTERN_DATE_TIME);
        }
    }

}
