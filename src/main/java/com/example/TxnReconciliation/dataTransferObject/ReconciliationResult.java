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

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();
        resultString.append("Buyer Transaction: ").append(buyerTransaction).append(", ");
        resultString.append("Supplier Transaction: ").append(supplierTransaction).append(", ");
        resultString.append("Category: ").append(matchCategory);
        return resultString.toString();
    }
}
