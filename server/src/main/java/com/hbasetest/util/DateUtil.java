package com.hbasetest.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getDateShow(Date date, String format) {
        SimpleDateFormat resultFormat = new SimpleDateFormat(format);
        return resultFormat.format(date);
    }
}
