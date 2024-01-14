package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.StringMatching;

public class billNoMatcher implements IFieldMatcher {
    private final IFieldTypeMatching fieldTypeMatching;

    public billNoMatcher() {
        this.fieldTypeMatching = new StringMatching();
    }
    @Override
    public boolean match(String buyerBillNo, String supplierBillNo) {
        return fieldTypeMatching.match(buyerBillNo,supplierBillNo);
    }

    @Override
    public double similarityScore(String value1, String value2) {
        return fieldTypeMatching.similarityScore(value1, value2);
    }
}
