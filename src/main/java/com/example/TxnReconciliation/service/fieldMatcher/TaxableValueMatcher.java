package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.NumberMatching;

public class TaxableValueMatcher implements IFieldMatcher {

    private final IFieldTypeMatching fieldTypeMatching;

    public TaxableValueMatcher(){
        this.fieldTypeMatching = new NumberMatching();
    }
    @Override
    public boolean match(String buyerTaxableValue, String supplierTaxableValue) {
        return fieldTypeMatching.match(buyerTaxableValue,supplierTaxableValue);
    }

    @Override
    public double getSimilarityScore(String value1, String value2) {
        return fieldTypeMatching.getSimilarityScore(value1, value2);
    }
}
