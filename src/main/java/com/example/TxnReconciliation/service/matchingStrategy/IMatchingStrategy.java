package com.example.TxnReconciliation.service.matchingStrategy;

import com.example.TxnReconciliation.model.Transaction;

public interface IMatchingStrategy {

    boolean match(Transaction txn1, Transaction txn2);

    double calculateSimilarityScore(Transaction txn1, Transaction txn2);
}
