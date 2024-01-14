package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.DateMatching;

public class dateMatcher implements IFieldMatcher {

    private final IFieldTypeMatching fieldTypeMatching;

    public dateMatcher(){
        this.fieldTypeMatching = new DateMatching();
    }
    @Override
    public boolean match(String buyerDate, String supplierDate) {
        return fieldTypeMatching.match(buyerDate, supplierDate);
    }

    @Override
    public double similarityScore(String value1, String value2) {
        return fieldTypeMatching.similarityScore(value1, value2);
    }

}
