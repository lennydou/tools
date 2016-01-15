package org.lendou.thread;

import java.text.SimpleDateFormat;

/**
 * Created by dyl on 12/24/15.
 */
public class TimeHelper {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd#HHmmss.SSS");
    public static String getCurTime() {
        return sdf.format(new java.util.Date());
    }
}
