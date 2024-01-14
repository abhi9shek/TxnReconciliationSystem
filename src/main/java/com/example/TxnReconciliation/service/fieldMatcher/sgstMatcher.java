package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.NumberMatching;

public class sgstMatcher implements IFieldMatcher {

    private final IFieldTypeMatching fieldTypeMatching;

    public sgstMatcher(){
        this.fieldTypeMatching = new NumberMatching();
    }
    @Override
    public boolean match(String buyerSgst, String supplierSgst) {
        return fieldTypeMatching.match(buyerSgst,supplierSgst);
    }

    @Override
    public double similarityScore(String value1, String value2) {
        return fieldTypeMatching.similarityScore(value1, value2);
    }

}
