package com.example.TxnReconciliation.service.fieldTypeMatcher;

import com.example.TxnReconciliation.service.PropertiesReader;
import com.example.TxnReconciliation.utils.DateConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.text.ParseException;

public class DateMatching implements IFieldTypeMatching {

    static final Logger logger = LoggerFactory.getLogger(String.valueOf(DateMatching.class));

    private static final double DATE_THRESHOLD;

    static {
        DATE_THRESHOLD = Double.parseDouble(PropertiesReader.getInstance().getProperty("DATE_THRESHOLD", "1"));
    }
    @Override
    public boolean match(String value1, String value2) {
        Date dt1 ;
        try {
            dt1 = DateConvertor.stringToSqlDate(value1);
        } catch (ParseException | NullPointerException e) {
            logger.error("Error parsing date field : {}", e.getMessage());
            return false;
        }

        Date dt2 ;
        try {
            dt2 = DateConvertor.stringToSqlDate(value2);
        } catch (ParseException | NullPointerException e) {
            logger.error("Error parsing date field : {}", e.getMessage());
            return false;
        }

        long diffInMillies = Math.abs(dt1.getTime() - dt2.getTime());
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
        if(diffInDays==0){
            return true;
        }
        return diffInDays <= DATE_THRESHOLD;
    }

    @Override
    public double getSimilarityScore(String value1, String value2) {
        Date dt1 ;
        try {
            dt1 = DateConvertor.stringToSqlDate(value1);
        } catch (ParseException | NullPointerException e) {
            logger.warn("Error parsing date field : {}", e.getMessage());
            return 0;
        }

        Date dt2 ;
        try {
            dt2 = DateConvertor.stringToSqlDate(value2);
        } catch (ParseException | NullPointerException e) {
            logger.warn("Error parsing date field : {}", e.getMessage());
            return 0;
        }

        logger.debug("dt1 & dt2 : {} {}", dt1.getTime(), dt2.getTime());
        long diffInMillies = Math.abs(dt1.getTime() - dt2.getTime());
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
        logger.debug("DiffInDays: {} ",diffInDays);
        return diffInDays;
    }
}
