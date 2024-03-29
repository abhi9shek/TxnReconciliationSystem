package com.example.TxnReconciliation.dataTransferObject;

import com.example.TxnReconciliation.model.Transaction;
import com.example.TxnReconciliation.model.enums.MatchCategory;

public class ReconciliationResult {

    private Transaction buyerTransaction;
    private Transaction supplierTransaction;
    private MatchCategory matchCategory;

    public ReconciliationResult(Transaction buyerTransaction, Transaction supplierTransaction, MatchCategory matchCategory) {
        this.buyerTransaction = buyerTransaction;
        this.supplierTransaction = supplierTransaction;
        this.matchCategory = matchCategory;
    }

    public Transaction getBuyerTransaction() {
        return buyerTransaction;
    }

    public Transaction getSupplierTransaction() {
        return supplierTransaction;
    }

    public MatchCategory getMatchCategory() {
        return matchCategory;
    }

}
