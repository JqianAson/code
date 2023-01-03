package com.demo.code.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: Jason
 * @create: 2021-01-19 10:51
 * @Description 时间工具类
 **/

public class DateUtils {

    public final static String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public final static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * String转Date
     *
     * @param data 传入的字符串格式日期 （yyyy-MM-dd HH:mm:ss）
     * @return Date
     */
    public static Date stringToDate(String data) {
        if (data == null || data.length() == 0) {
            return null;
        }
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
        try {
            date = dateFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Date转字符串
     *
     * @param date 时间
     * @return 字符串
     */
    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern == null ? PATTERN : pattern);
        return formatter.format(date);
    }

    /**
     * Date转字符串
     *
     * @param date 时间
     * @return 字符串
     */
    public static String dateToString(Date date) {
        return dateToString(date, null);
    }

    /**
     * 获取当前时间（转换为字符串格式：yyyy-MM-dd HH:mm:ss）
     *
     * @return
     */
    public static String getStringNowDate() {
        return dateToString(new Date());
    }

    /**
     * 根据输入时间获取指定时间
     *
     * @param dateFormatter 时间
     * @return 返回值
     */
    public static LocalDate getBeforeTime(String dateFormatter) {

        if (!StringUtils.isEmpty(dateFormatter)) {
            SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
            Date parse;
            try {
                parse = format.parse(dateFormatter);
                int days = (int) ((System.currentTimeMillis() - parse.getTime()) / (1000 * 3600 * 24));
                return LocalDate.now().plusDays(-days);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return LocalDate.now().plusDays(-1);
    }

    /**
     * Date转字符串，如果传入时间为空，则默认使用当前时间
     *
     * @param date    时间
     * @param pattern 时间格式
     * @return 时间字符串
     */
    public static String dateToStringDefaultNewDate(Date date, String pattern) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern == null ? PATTERN : pattern);
        return formatter.format(date);
    }

    public static Date getCurDate() {
        return new Date();
    }

    public static final Date convertToDate(String dateStr, String pattern) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.parse(dateStr);
        } catch (Exception var3) {
            throw new RuntimeException("日期格式化转换异常: Str To Date", var3);
        }
    }

    public static Date getTradingEndDate(Double monthParam) {
        return getTradingEndDate(new Date(), monthParam);
    }

    public static Date getTradingEndDate(Date startTime, Double aMonthParam) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MONTH, aMonthParam.intValue());
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static Date getTradingEndDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 获取指定时间之后指定小时数的时间
     *
     * @param specifyTime 指定时间
     * @param afterNum    之后的天数
     * @return 指定时间之后指定小时数的时间
     */
    public static Date getSpecifyTimeAfterDate(Date specifyTime, int afterNum) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(specifyTime);
        instance.add(Calendar.DAY_OF_YEAR, afterNum);
        return instance.getTime();
    }

    public static Date getSpecifyTimeBeforeDate(Date specifyTime, int beforeNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(specifyTime);
        calendar.add(Calendar.DAY_OF_YEAR, beforeNum);
        return calendar.getTime();
    }

    /**
     * 获取当前时间至指定时间的秒数
     *
     * @param date 指定时间
     */
    public static long getSecondsBetweenNowAndDate(Date date) {
        return (date.getTime() - System.currentTimeMillis()) / 1000;
    }


    /**
     * 获取当前时间至次日0时的毫秒值
     *
     * @return 毫秒值
     */
    public static Long getTomorrowZeroMillis() {
        // 当前时间毫秒数
        long current = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long tomorrowZero = calendar.getTimeInMillis();
        return tomorrowZero - current;
    }
}
