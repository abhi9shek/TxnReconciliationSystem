package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import com.example.TxnReconciliation.service.fieldTypeMatcher.StringMatching;

public class BillNoMatcher implements IFieldMatcher {
    private final IFieldTypeMatching fieldTypeMatching;

    public BillNoMatcher() {
        this.fieldTypeMatching = new StringMatching();
    }
    @Override
    public boolean match(String buyerBillNo, String supplierBillNo) {
        return fieldTypeMatching.match(buyerBillNo,supplierBillNo);
    }

    @Override
    public double getSimilarityScore(String value1, String value2) {
        return fieldTypeMatching.getSimilarityScore(value1, value2);
    }
}
