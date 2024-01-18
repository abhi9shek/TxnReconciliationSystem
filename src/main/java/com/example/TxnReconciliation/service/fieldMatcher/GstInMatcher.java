package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.StringMatching;

public class GstInMatcher implements IFieldMatcher {

    private final IFieldTypeMatching fieldTypeMatching;

    public GstInMatcher(){
        this.fieldTypeMatching = new StringMatching();
    }
    @Override
    public boolean match(String buyerGstIn, String supplierGstIn) {
        return fieldTypeMatching.match(buyerGstIn,supplierGstIn);
    }

    @Override
    public double getSimilarityScore(String value1, String value2) {
        return fieldTypeMatching.getSimilarityScore(value1, value2);
    }
}
