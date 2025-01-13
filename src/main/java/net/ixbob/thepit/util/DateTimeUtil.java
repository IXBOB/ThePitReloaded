package net.ixbob.thepit.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    public static String getCurrentTimeStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
