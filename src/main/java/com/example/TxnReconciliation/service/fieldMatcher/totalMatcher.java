package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.NumberMatching;

public class totalMatcher implements IFieldMatcher{

    private final IFieldTypeMatching fieldTypeMatching;

    public totalMatcher(){
        this.fieldTypeMatching = new NumberMatching();
    }
    @Override
    public boolean match(String buyerTotal, String supplierTotal) {
        return fieldTypeMatching.match(buyerTotal,supplierTotal);
    }

    @Override
    public double similarityScore(String value1, String value2) {
        return fieldTypeMatching.similarityScore(value1, value2);
    }
}
