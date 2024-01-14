package com.example.TxnReconciliation.service.fieldTypeMatcher;

import com.example.TxnReconciliation.service.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberMatching implements IFieldTypeMatching {

    static final Logger logger = LoggerFactory.getLogger(String.valueOf(NumberMatching.class));

    // To be read from config file
    private static final double NUMBER_THRESHOLD ;

    static {
        NUMBER_THRESHOLD = Double.parseDouble(PropertiesReader.getInstance().getProperty("NUMBER_THRESHOLD", "0.9"));
    }
    @Override
    public boolean match(String value1, String value2) {
        try {
            double d1 = Double.parseDouble(value1);
            double d2 = Double.parseDouble(value2);
            if(d1==d2){
                return true;
            }
            return 1 - Math.abs(d1 - d2) / Math.max(Math.abs(d1), Math.abs(d2)) >= NUMBER_THRESHOLD;
        } catch (NumberFormatException e) {
            logger.warn("Invalid number format {}", e.getMessage());
            return false;
        }
    }

    @Override
    public double similarityScore(String value1, String value2) {
        double d1 = Double.parseDouble(value1);
        double d2 = Double.parseDouble(value2);
        return 1 - Math.abs(d1 - d2) / Math.max(Math.abs(d1), Math.abs(d2));
    }
}
