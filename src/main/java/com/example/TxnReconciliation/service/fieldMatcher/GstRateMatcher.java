package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.NumberMatching;

public class GstRateMatcher implements IFieldMatcher {

    private final IFieldTypeMatching fieldTypeMatching;

    public GstRateMatcher(){
        this.fieldTypeMatching = new NumberMatching();
    }
    @Override
    public boolean match(String buyerGstRate, String supplierGstRate) {
        return fieldTypeMatching.match(buyerGstRate,supplierGstRate);
    }

    @Override
    public double getSimilarityScore(String value1, String value2) {
        return fieldTypeMatching.getSimilarityScore(value1, value2);
    }
}
