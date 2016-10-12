package com.lendou;

import org.apache.commons.lang3.Validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间帮助类
 */
public class TimeUtils {

    private static SimpleDateFormat detailSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat simpleSdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获得当前时间的字符串形式
     *
     * @return 当前时间的字符串形式
     */
    public static String getNow() {
        return getTime(new Date());
    }

    /**
     * 获得传输时间的字符串形式
     *
     * @param date 时间
     * @return 字符串形式的时间
     */
    public static String getTime(Date date) {
        Validate.notNull(date);
        return detailSdf.format(date);
    }

    /**
     * 获取日期 可通过month修改日期
     *
     * @param month 月份
     * @return
     */
    public static String getTimeFromNow(int month) {

        // 获取当前日期并设置
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // 给当前日期加几个月
        calendar.add(Calendar.MONTH, month);

        // 格式化日期
        Date date = new Date(calendar.getTime().getTime());

        return simpleSdf.format(date);
    }

    /**
     * 把日期字符串转成date
     *
     * @param dateStr 日期字符串
     * @return 参数日期字符串对应的 Date 类型
     */
    public static Date parse(String dateStr) throws ParseException {
        Validate.notEmpty(dateStr);

        return dateStr.length() > 12 ? detailSdf.parse(dateStr):simpleSdf.parse(dateStr);
    }

    /**
     * 计算两个日期之间的月数
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pattern 格式化字符串
     * @return
     * @throws ParseException
     */

    public static int countMonths(String startDate, String endDate, String pattern) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        // 获取日历构造器
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        // 设置时间
        startCalendar.setTime(sdf.parse(startDate));
        endCalendar.setTime(sdf.parse(endDate));

        // 结束日期-开始日期
        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);

        // 如果开始日期<结束日期
        if (year < 0) {
            year = -year;
            return year * 12 + startCalendar.get(Calendar.MONTH) - endCalendar.get(Calendar.MONTH);
        }

        return year * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    }
}
