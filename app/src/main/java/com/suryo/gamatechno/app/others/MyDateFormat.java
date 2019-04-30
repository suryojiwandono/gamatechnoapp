package com.suryo.gamatechno.app.others;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyDateFormat {

    private static final String[] MONTHS = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" };
    private static final String[] MONTHS_SHORT = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des" };
    private static final String[] DAYS = {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu" };
    private static final String[] DAYS_SHORT = {"", "Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab" };

    private static DateFormatSymbols getLocale() {
        DateFormatSymbols symbols = new DateFormatSymbols();
        symbols.setMonths(MONTHS);
        symbols.setShortMonths(MONTHS_SHORT);
        symbols.setWeekdays(DAYS);
        symbols.setShortWeekdays(DAYS_SHORT);
        return symbols;
    }

    public static String format(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
        return formatter.format(Calendar.getInstance().getTime());
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
        return formatter.format(date);
    }

    public static String format(String date, String oldPattern, String newPattern) {
        if (date.equals("")) return "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(oldPattern, Locale.US);
            SimpleDateFormat formatter1 = new SimpleDateFormat(newPattern, Locale.US);
            return formatter1.format(formatter.parse(date));
        } catch (ParseException e) {
            return e.getMessage();
        }
    }

    public static String formatLocale(String date, String oldPattern, String newPattern) {
        if (date.equals("")) return "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(oldPattern, getLocale());
            SimpleDateFormat formatter1 = new SimpleDateFormat(newPattern, getLocale());
            return formatter1.format(formatter.parse(date));
        } catch (ParseException e) {
            return e.getMessage();
        }
    }

    public static abstract class Pattern {
        public static final String databaseDateTime = "yyyy-MM-dd HH:mm:ss";    // 2018-12-02 10:00:00
        public static final String databaseDate = "yyyy-MM-dd";                 // 2018-12-02
        public static final String databaseID = "yyMMddHHmmss";                 // 181202100000

        public static final String day = "dd";                                  // 01
        public static final String month = "MM";                                // 12
        public static final String year = "yyyy";                               // 2018
        public static final String time = "HH:mm:ss";                           // 10:00:00
        public static final String timeMinute = "HH:mm";                           // 10:00:00

        public static final String labelMonthDate = "MMMM dd";                  // April 02
        public static final String labelMonthYear = "MMMM yyyy";                // April 2018
        public static final String labelDate = "dd MMMM yyyy";                  // 02 April 2018
        public static final String labelDateDay = "EEEE, dd MMMM yyyy";                  //Wednesday, 02 April 2018
        public static final String labelDateShort = "dd MMM yy";                // 02 Apr 18
        public static final String labelDateTimeSecond = "dd MMMM yyyy HH:mm:ss";     // 02 April 2018 10:00:00
        public static final String labelDateTimeMinute = "dd MMMM yyyy HH:mm";      // 02 April 2018 10:00

    }

    public static long dateToLong(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat(Pattern.databaseDateTime, Locale.US);
        try {
            Date d = f.parse(datetime);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
