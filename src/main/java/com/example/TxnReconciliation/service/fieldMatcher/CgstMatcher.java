package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.NumberMatching;

public class CgstMatcher implements IFieldMatcher {
    private final IFieldTypeMatching fieldTypeMatching;

    public CgstMatcher(){
        this.fieldTypeMatching = new NumberMatching();
    }
    @Override
    public boolean match(String buyerCgst, String supplierCgst) {
        return fieldTypeMatching.match(buyerCgst,supplierCgst);
    }

    @Override
    public double getSimilarityScore(String value1, String value2) {
        return fieldTypeMatching.getSimilarityScore(value1, value2);
    }
}
