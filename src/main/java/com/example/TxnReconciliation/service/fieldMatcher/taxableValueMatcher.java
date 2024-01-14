package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.NumberMatching;

public class taxableValueMatcher implements IFieldMatcher {

    private final IFieldTypeMatching fieldTypeMatching;

    public taxableValueMatcher(){
        this.fieldTypeMatching = new NumberMatching();
    }
    @Override
    public boolean match(String buyerTaxableValue, String supplierTaxableValue) {
        return fieldTypeMatching.match(buyerTaxableValue,supplierTaxableValue);
    }

    @Override
    public double similarityScore(String value1, String value2) {
        return fieldTypeMatching.similarityScore(value1, value2);
    }
}
