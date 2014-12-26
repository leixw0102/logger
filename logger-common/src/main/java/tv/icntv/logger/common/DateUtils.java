package tv.icntv.logger.common;/*
 * Copyright 2014 Future TV, Inc.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/05/23
 * Time: 17:53
 */
public class DateUtils {

    private static String formatFull="yyyy-MM-dd HH:mm:ss SSS";
    private static DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(formatFull);
    private static String defaultFormat="yyyyMMdd HHmmss";
    public static String getFormatDate( Date date){
        try {
            DateTime dateTime=new DateTime(date);
            return dateTime.toString(defaultFormat);
        } catch (Exception e) {
            return "";
        }
    }
    public static String getFormatDate(String dateStr){
        return getFormatDate(dateStr,defaultFormat);
    }

    public static String getFormatDate(String date,String toformat){
        return getFormatDate(date,toformat,dateTimeFormatter);
    }
    public static String getFormatDate(String date,String toformat,DateTimeFormatter fromFormat){
        if(Strings.isNullOrEmpty(date)){
            return "";
        }
        DateTime time = DateTime.parse(date,fromFormat);
        return time.toString(toformat);
    }

    public static String getFormatDate(String date,DateTimeFormatter formatter){
        return getFormatDate(date,defaultFormat,formatter);
    }

    public static String getDay(String format){
        return DateTime.now().toString(format);
    }

    public static DateTime getDay(int days){
        return DateTime.now().plusDays(days);
    }

    public static DateTime getPlusMonth(int month){
        return DateTime.now().plusMonths(month);
    }

    public static List<String> getDay(DateTime start,DateTime end,String format){
        List<String> list = Lists.newArrayList();
        int days = Days.daysBetween(start,end).getDays();
        for(int i=0;i<=days;i++){
            list.add(start.plusDays(i).toString(format));
        }
        return list;
    }

    public static void main(String []args){
//        System.out.println(getFormatDate("2014-10-22 00:32:21",defaultFormat,DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        DateTime dateTime = DateTime.now();
        DateTime end = DateTime.now().plusMonths(-1);
        for(int i=0;i<Days.daysBetween(end,dateTime).getDays();i++){
            System.out.println(end.plusDays(i).toLocalDate());

        }
//        System.out.println(dateTime.toLocalDate() + "\t"+end.toLocalDate());
//        Period p = new Period(DateTime.now().plusMonths(-1),dateTime);
//        Days days = Days.daysBetween(end,dateTime);
//        days.getDays();
//        while (true){
//            Days days1 = days.dividedBy(1);
//            System.out.println(days1.toString());
//        }
//        Days.daysBetween()
    }
}
