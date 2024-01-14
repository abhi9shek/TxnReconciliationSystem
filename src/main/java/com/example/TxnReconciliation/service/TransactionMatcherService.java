package com.example.TxnReconciliation.service;

import com.example.TxnReconciliation.model.Transaction;
import com.example.TxnReconciliation.model.enums.MatchType;
import com.example.TxnReconciliation.service.matchingStrategy.ExactMatchingStrategy;
import com.example.TxnReconciliation.service.matchingStrategy.PartialMatchingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionMatcherService {

    static final Logger logger = LoggerFactory.getLogger(String.valueOf(TransactionMatcherService.class));

    private final ExactMatchingStrategy exactMatch;

    private final PartialMatchingStrategy partialMatch;
    public TransactionMatcherService() {
        this.exactMatch = new ExactMatchingStrategy();
        this.partialMatch = new PartialMatchingStrategy();
    }

    public MatchType compareTransactions(Transaction buyerTransaction, Transaction supplierTransaction) {
        logger.debug("Inside compare transactions");
        if (exactMatch.match(buyerTransaction,supplierTransaction)) {
            return MatchType.EXACT;
        }

        if (partialMatch.match(buyerTransaction, supplierTransaction)) {
            return MatchType.PARTIAL;
        }

        return MatchType.NONE;
    }

    public double calculateMatchScore(Transaction buyerTransaction , Transaction supplierTransaction){
        return partialMatch.calculateSimilarityScore(buyerTransaction, supplierTransaction);
    }
}
