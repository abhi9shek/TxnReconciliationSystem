package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.NumberMatching;

public class gstRateMatcher implements IFieldMatcher {

    private final IFieldTypeMatching fieldTypeMatching;

    public gstRateMatcher(){
        this.fieldTypeMatching = new NumberMatching();
    }
    @Override
    public boolean match(String buyerGstRate, String supplierGstRate) {
        return fieldTypeMatching.match(buyerGstRate,supplierGstRate);
    }

    @Override
    public double similarityScore(String value1, String value2) {
        return fieldTypeMatching.similarityScore(value1, value2);
    }
}
