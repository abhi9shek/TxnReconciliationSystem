package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.NumberMatching;

public class IgstMatcher implements IFieldMatcher {

    private final IFieldTypeMatching fieldTypeMatching;

    public IgstMatcher(){
        this.fieldTypeMatching = new NumberMatching();
    }
    @Override
    public boolean match(String buyerIgst, String supplierIgst) {
        return fieldTypeMatching.match(buyerIgst,supplierIgst);
    }

    @Override
    public double getSimilarityScore(String value1, String value2) {
        return fieldTypeMatching.getSimilarityScore(value1, value2);
    }

}
