package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.StringMatching;

public class gstInMatcher implements IFieldMatcher {

    private final IFieldTypeMatching fieldTypeMatching;

    public gstInMatcher(){
        this.fieldTypeMatching = new StringMatching();
    }
    @Override
    public boolean match(String buyerGstIn, String supplierGstIn) {
        return fieldTypeMatching.match(buyerGstIn,supplierGstIn);
    }

    @Override
    public double similarityScore(String value1, String value2) {
        return fieldTypeMatching.similarityScore(value1, value2);
    }
}
