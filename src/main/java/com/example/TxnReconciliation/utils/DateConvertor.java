package com.example.TxnReconciliation.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateConvertor {

    public static Date stringToSqlDate(String dateString) throws ParseException {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        // Define multiple format patterns to handle different date formats
        String[] patterns = {"MM/dd/yy", "dd-MM-yyyy", "yyyy-MM-dd"};

        for (String pattern : patterns) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                java.util.Date utilDate = dateFormat.parse(dateString);
                return new java.sql.Date(utilDate.getTime());
            } catch (ParseException e) {
                // Try the next format if parsing fails
            }
        }

        return null;
    }
}
