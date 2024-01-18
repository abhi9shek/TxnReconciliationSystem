package com.example.TxnReconciliation.service.fieldMatcher;

public interface IFieldMatcher {

    boolean match(String val1, String val2);

    double getSimilarityScore(String value1, String value2);
}
