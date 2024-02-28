package fpt.fall23.onlearn.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class DateTimeUtils {

    public static OffsetDateTime toOffsetDateTime(String datetimeString, String format) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
            LocalDateTime localDateTime = LocalDateTime.parse(datetimeString + " 00:00:00 +00:00", dtf);
            return OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static LocalDateTime toLocalDateTime(OffsetDateTime time, TimeZone timeZone) {
        if (time == null || timeZone == null) {
            return null;
        }

        ZoneId zoneId = ZoneId.of(timeZone.getID());
        return time.atZoneSameInstant(zoneId)
                .toLocalDateTime();
    }

    public static String toString(LocalDateTime date, String format) {
        if (date == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return date.format(formatter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String toString(OffsetDateTime time, TimeZone timeZone, String format) {
        try {
            LocalDateTime localDateTime = toLocalDateTime(time, timeZone);
            return toString(localDateTime, format);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static LocalDateTime convertStringToLocalDate(String timestampString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return LocalDateTime.parse(timestampString, formatter);
    }

}
