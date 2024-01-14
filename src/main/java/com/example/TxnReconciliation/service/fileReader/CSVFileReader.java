package com.example.TxnReconciliation.service.fileReader;

import com.example.TxnReconciliation.constants.TxnFileHeaders;
import com.example.TxnReconciliation.model.Transaction;
import com.example.TxnReconciliation.service.PropertiesReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CSVFileReader implements IFileReader {
    static final Logger logger = LoggerFactory.getLogger(String.valueOf(CSVFileReader.class));

    private static final int BATCH_SIZE;

    static {
        BATCH_SIZE = Integer.parseInt(PropertiesReader.getInstance().getProperty("BATCH_SIZE", "100"));
    }

    @Override
    public List<Transaction> readTransactions(String filePath, String txnType) {
        List<Transaction> transactions = new ArrayList<>();
        Set<String> uniqueTransactionKeys = new HashSet<>();

        try (FileReader fileReader = new FileReader(filePath);
             CSVParser csvParser = CSVFormat.EXCEL.withHeader().parse(fileReader)) {
             List<CSVRecord> csvRecords = csvParser.getRecords();
             int totalRecords = csvRecords.size();

            for (int startIndex = 0; startIndex < totalRecords; startIndex += BATCH_SIZE) {
                int endIndex = Math.min(startIndex + BATCH_SIZE, totalRecords);
                List<CSVRecord> batchRecords = csvRecords.subList(startIndex, endIndex);

                for (CSVRecord csvRecord : batchRecords) {

                    String gstIn = csvRecord.get(TxnFileHeaders.GSTIN);
                    String date = csvRecord.get(TxnFileHeaders.DATE);
                    String billNo = csvRecord.get(TxnFileHeaders.BILLNO);

                    // Create a key based on GSTIN, date, and bill number
                    String transactionKey = createTransactionKey(gstIn, date, billNo);

                    // duplicate check
                    if (uniqueTransactionKeys.contains(transactionKey)) {
                        logger.warn("Duplicate transaction found. Skipping: GSTIN={}, Date={}, BillNo={}", gstIn, date, billNo);
                        continue;
                    }

                    uniqueTransactionKeys.add(transactionKey);

                    Double gstRate = parseDoubleWithValidation(csvRecord.get(TxnFileHeaders.GSTRATE), TxnFileHeaders.GSTRATE);
                    Double taxableValue = parseDoubleWithValidation(csvRecord.get(TxnFileHeaders.TAXABLEVALUE).replace(",", ""), TxnFileHeaders.TAXABLEVALUE);
                    Double igst = parseDoubleWithValidation(csvRecord.get(TxnFileHeaders.IGST).replace(",", ""), TxnFileHeaders.IGST);
                    Double cgst = parseDoubleWithValidation(csvRecord.get(TxnFileHeaders.CGST).replace(",", ""), TxnFileHeaders.CGST);
                    Double sgst = parseDoubleWithValidation(csvRecord.get(TxnFileHeaders.SGST).replace(",", ""), TxnFileHeaders.SGST);
                    Double total = parseDoubleWithValidation(csvRecord.get(TxnFileHeaders.TOTAL).replace(",", ""), TxnFileHeaders.TOTAL);

                    transactions.add(new Transaction(gstIn, date, billNo, gstRate, taxableValue, igst, cgst, sgst, total, txnType));
                }
            }

        } catch (IOException | NumberFormatException e) {
            logger.error("Processing failed due to : {}", e.getMessage());
        }

        return transactions;
    }

    private Double parseDoubleWithValidation(String value, String fieldName) {
        try {
            return value!=null ? Double.parseDouble(value): 0.0;
        } catch (NumberFormatException e) {
            logger.warn("Invalid value for {}: {}", fieldName, value);
            return 0.0;
        }
    }

    private String createTransactionKey(String gstIn, String date, String billNo) {
        return gstIn + "_" + date + "_" + billNo;
    }
}
