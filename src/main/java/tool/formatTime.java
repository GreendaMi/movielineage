package tool;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by GreendaMi on 2016/11/30.
 */

public class formatTime {
    /*
 * 毫秒转化时分秒毫秒
 */
    public static String formatTime(Long ms) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。

        String hms = formatter.format(ms - TimeZone.getDefault().getRawOffset());
        return hms;
    }
}
