package fpt.fall23.onlearn.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date addMonthsToDate(Date startDate, int monthsToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, monthsToAdd);
        return calendar.getTime();
    }
}
