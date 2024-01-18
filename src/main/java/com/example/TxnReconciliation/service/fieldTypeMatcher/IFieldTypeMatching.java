package com.example.TxnReconciliation.service.fieldTypeMatcher;

public interface IFieldTypeMatching {

    boolean match(String value1, String value2);

    double getSimilarityScore(String value1, String value2);
}
