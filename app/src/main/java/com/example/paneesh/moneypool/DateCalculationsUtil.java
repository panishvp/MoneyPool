package com.example.paneesh.moneypool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCalculationsUtil {

    public static Date stringToDateParse (String ipDate){
        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
        Calendar IPdate = Calendar.getInstance();
        try {
            IPdate.setTime(format.parse(ipDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return IPdate.getTime();
    }

    public static Date addMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        return  cal.getTime();

    }

    public static java.sql.Date stringToSQLDate (String ipDate){
        java.util.Date startDate = DateCalculationsUtil.stringToDateParse(ipDate);
        java.sql.Date startDateSql = new java.sql.Date(startDate.getTime());
        return startDateSql;
    }
}
