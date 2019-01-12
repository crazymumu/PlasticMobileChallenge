package com.example.mswu.plasticmobilechallenge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public String datetime = null;

    /**
     * Convert datetime property to desired format.
     *
     * @return Formatted time in string on success. Null on failure.
     */
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss.SSSSSS");

        try {
            final Date date = format.parse(datetime);
            format = new SimpleDateFormat("hh:mm:ss");
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
