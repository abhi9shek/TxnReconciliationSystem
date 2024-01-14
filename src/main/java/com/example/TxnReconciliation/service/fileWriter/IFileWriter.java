package com.example.TxnReconciliation.service.fileWriter;

import com.example.TxnReconciliation.dataTransferObject.ReconciliationResult;

import java.util.List;

public interface IFileWriter {

    void writeResults(List<ReconciliationResult> results, String filePath);
}
