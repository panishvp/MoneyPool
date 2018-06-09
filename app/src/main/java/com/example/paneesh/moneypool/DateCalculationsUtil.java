package com.example.paneesh.moneypool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCalculationsUtil {

   private SimpleDateFormat format = new SimpleDateFormat(Utils.datePattern);

    public Date stringToDateParse (String ipDate){

        Calendar IPdate = Calendar.getInstance();
        try {
            IPdate.setTime(format.parse(ipDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date ddd = IPdate.getTime();
        return IPdate.getTime();
    }

    public  Date addMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        return  cal.getTime();

    }

    public  Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }

    public java.sql.Date stringToSQLDate (String ipDate){

        java.util.Date startDate = stringToDateParse(ipDate);
        java.sql.Date startDateSql = new java.sql.Date(startDate.getTime());
        return startDateSql;
    }


}
