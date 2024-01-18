package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.DateMatching;

public class DateMatcher implements IFieldMatcher {

    private final IFieldTypeMatching fieldTypeMatching;

    public DateMatcher(){
        this.fieldTypeMatching = new DateMatching();
    }
    @Override
    public boolean match(String buyerDate, String supplierDate) {
        return fieldTypeMatching.match(buyerDate, supplierDate);
    }

    @Override
    public double getSimilarityScore(String value1, String value2) {
        return fieldTypeMatching.getSimilarityScore(value1, value2);
    }

}
