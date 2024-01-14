package com.example.TxnReconciliation.service.fileWriter;

import com.example.TxnReconciliation.dataTransferObject.ReconciliationResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class CSVFileWriter implements IFileWriter {

    static final Logger logger = LoggerFactory.getLogger(String.valueOf(CSVFileWriter.class));

    @Override
    public void writeResults(List<ReconciliationResult> results, String outputPath) {
        try (FileWriter fileWriter = new FileWriter(outputPath);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.EXCEL.withHeader("Buyer_GSTIN", "Buyer_Date", "Buyer_BillNo",
                     "Supplier_GSTIN", "Supplier_Date", "Supplier_BillNo", "Match_Category"))) {

            for (ReconciliationResult result : results) {
                csvPrinter.printRecord(
                        (result.getBuyerTransaction() != null) ? result.getBuyerTransaction().getGstIn() : "",
                        (result.getBuyerTransaction() != null) ? result.getBuyerTransaction().getDate() : "",
                        (result.getBuyerTransaction() != null) ? result.getBuyerTransaction().getBillNo() : "",
                        (result.getSupplierTransaction() != null) ? result.getSupplierTransaction().getGstIn() : "",
                        (result.getSupplierTransaction() != null) ? result.getSupplierTransaction().getDate() : "",
                        (result.getSupplierTransaction() != null) ? result.getSupplierTransaction().getBillNo() : "",
                        result.getMatchCategory().toString()
                );
            }

            csvPrinter.flush();
        } catch (IOException e) {
            logger.error("Write failed due to {}", e.getMessage());
            throw new RuntimeException("Write failed due to " + e.getMessage());
        }
    }
}
