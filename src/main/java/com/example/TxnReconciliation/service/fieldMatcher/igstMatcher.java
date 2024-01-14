package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.NumberMatching;

public class igstMatcher implements IFieldMatcher {

    private final IFieldTypeMatching fieldTypeMatching;

    public igstMatcher(){
        this.fieldTypeMatching = new NumberMatching();
    }
    @Override
    public boolean match(String buyerIgst, String supplierIgst) {
        return fieldTypeMatching.match(buyerIgst,supplierIgst);
    }

    @Override
    public double similarityScore(String value1, String value2) {
        return fieldTypeMatching.similarityScore(value1, value2);
    }

}
