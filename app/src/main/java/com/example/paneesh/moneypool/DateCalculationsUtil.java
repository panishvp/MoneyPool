package com.example.paneesh.moneypool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCalculationsUtil {

   private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);

    public Date stringToDateParse (String ipDate){

        Calendar IPdate = Calendar.getInstance();
        try {
            IPdate.setTime(simpleDateFormat.parse(ipDate));
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

        Date startDate = null;
        java.sql.Date startDateSql = null;
        if (ipDate != null){
            try {
                startDate = simpleDateFormat.parse(ipDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            startDateSql = new java.sql.Date(startDate.getTime());
        }

        return startDateSql;
    }


}
