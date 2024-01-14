package com.example.TxnReconciliation.dataTransferObject;

public class TxnCategorisationRequest {

    private String buyerTxnsFilePath;

    private String supplierTxnsFilePath;

    private String reconciliationFilePath;

    public String getBuyerTxnsFilePath() {
        return buyerTxnsFilePath;
    }

    public String getSupplierTxnsFilePath() {
        return supplierTxnsFilePath;
    }

    public String getReconciliationFilePath(){
        return reconciliationFilePath;
    }
}
