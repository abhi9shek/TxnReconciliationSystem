package com.example.TxnReconciliation.service.matchingStrategy;

import com.example.TxnReconciliation.model.Transaction;

public interface IMatchingStrategy {

    boolean match(Transaction txn1, Transaction txn2);

    double getSimilarityScore(Transaction txn1, Transaction txn2);
}
