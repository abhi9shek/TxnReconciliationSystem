package com.example.TxnReconciliation.service.fileReader;

import com.example.TxnReconciliation.model.Transaction;

import java.util.List;

public interface IFileReader {

    List<Transaction> readTransactions(String filePath, String txnType);
}
