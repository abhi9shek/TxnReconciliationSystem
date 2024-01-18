package com.example.TxnReconciliation.service.fieldTypeMatcher;

import com.example.TxnReconciliation.service.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringMatching implements IFieldTypeMatching {

    static final Logger logger = LoggerFactory.getLogger(String.valueOf(StringMatching.class));

    private static final double STRING_THRESHOLD;

    static {
        STRING_THRESHOLD = Double.parseDouble(PropertiesReader.getInstance().getProperty("STRING_THRESHOLD", "0.8"));
    }

    private final com.example.TxnReconciliation.utils.StringMatching stringMatching = new com.example.TxnReconciliation.utils.StringMatching();
    @Override
    public boolean match(String value1, String value2) {
        if(value1.equals(value2)){
            return true;
        }
        int distanceBetweenStrings = stringMatching.getDistanceBetweenStrings(value1, value2);
        return 1 - ((double) distanceBetweenStrings)/(double) Math.max(value1.length(), value2.length()) >= STRING_THRESHOLD;
    }

    @Override
    public double getSimilarityScore(String value1, String value2) {
        int distanceBetweenStrings = stringMatching.getDistanceBetweenStrings(value1, value2);
        return 1 - ((double) distanceBetweenStrings)/(double) Math.max(value1.length(), value2.length());
    }

}
