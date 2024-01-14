package com.example.TxnReconciliation.service;

import com.example.TxnReconciliation.dao.TxnReconciliationDAO;
import com.example.TxnReconciliation.dao.mapper.ReconciliationResMapper;
import com.example.TxnReconciliation.model.Transaction;
import com.example.TxnReconciliation.model.TxnReconciliation;
import com.example.TxnReconciliation.model.enums.MatchCategory;
import com.example.TxnReconciliation.dataTransferObject.ReconciliationResult;
import com.example.TxnReconciliation.model.enums.MatchType;
import com.example.TxnReconciliation.model.enums.TxnType;
import com.example.TxnReconciliation.dao.TxnDAO;
import com.example.TxnReconciliation.service.fileFactory.CSVFileFactory;
import com.example.TxnReconciliation.service.fileFactory.IFileFactory;
import com.example.TxnReconciliation.service.fileReader.IFileReader;
import com.example.TxnReconciliation.service.fileWriter.IFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategorisationEngineService {

    static final Logger logger = LoggerFactory.getLogger(String.valueOf(CategorisationEngineService.class));

    private final TransactionMatcherService transactionMatcher;

    private final TxnDAO txnDAO;

    private final TxnReconciliationDAO txnReconciliationDAO;

    private final ReconciliationResMapper reconciliationResMapper;

    public CategorisationEngineService(TxnDAO txnDAO, TxnReconciliationDAO txnReconciliationDAO, ReconciliationResMapper reconciliationResMapper){
        this.transactionMatcher = new TransactionMatcherService();
        this.txnDAO = txnDAO;
        this.txnReconciliationDAO = txnReconciliationDAO;
        this.reconciliationResMapper = reconciliationResMapper;
    }

    IFileFactory fileFactory = new CSVFileFactory();

    List<Transaction> processFiles(String filePath, String txnType){
        // Create a file reader
        IFileReader fileReader = fileFactory.createFileReader();

        List<Transaction> transactions = fileReader.readTransactions(filePath,txnType);
        List<Transaction> validTxns = validationsOnTxns(transactions);
        logger.info("Writing data to DB");
        try {
            txnDAO.saveAll(validTxns);
            logger.info("Data write to DB successful");
        }catch(Exception e){
            logger.error("Error writing data to DB : {}", e.getMessage());
            throw new RuntimeException("Error writing data to DB!");
        }

        return validTxns;
    }

    private Set<String> getExistingTransactionKeys() {
        logger.info("Fetching existing transactions");
        List<Transaction> existingTransactions = txnDAO.findAll();
        Set<String> existingTransactionKeys = new HashSet<>();

        for (Transaction existingTransaction : existingTransactions) {
            String transactionKey = existingTransaction.getGstIn() + "_" + existingTransaction.getDate() + "_" + existingTransaction.getBillNo() + "_" + existingTransaction.getTxnType();
            existingTransactionKeys.add(transactionKey);
        }

        return existingTransactionKeys;
    }

    List<Transaction> validationsOnTxns(List<Transaction> transactions) {
        Set<String> existingTransactionKeys = getExistingTransactionKeys();
        List<Transaction> validTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            String transactionKey = transaction.getGstIn() + "_" + transaction.getDate() + "_" + transaction.getBillNo() + "_" + transaction.getTxnType();

            // Check if the transaction is already existing, skip it
            if (existingTransactionKeys.contains(transactionKey)) {
                logger.info("Transaction already exists. Skipping: GSTIN={}, Date={}, BillNo={}", transaction.getGstIn(), transaction.getDate(), transaction.getBillNo());
                continue;
            }

            existingTransactionKeys.add(transactionKey);
            validTransactions.add(transaction);
        }
        return validTransactions;
    }

    public void processReconciliationResults(String buyerFilePath, String supplierFilePath, String reconciliationFilePath){

        // Create a file writer
        IFileWriter fileWriter = fileFactory.createFileWriter();

        logger.info("Reading buyer transaction file ");
        List<Transaction> buyerTransactions = processFiles(buyerFilePath, String.valueOf(TxnType.BUYER));

        logger.info("Reading supplier transaction file ");
        List<Transaction> supplierTransactions = processFiles(supplierFilePath, String.valueOf(TxnType.SUPPLIER));

        List<ReconciliationResult> reconciliationResults = reconcileTransactions(buyerTransactions,supplierTransactions);

        List<TxnReconciliation> txnReconciliations = reconciliationResMapper.reconciliationResultToTxnReconciliationMapper(reconciliationResults);

        logger.info("Writing reconciliation results to DB");
        try {
            txnReconciliationDAO.saveAll(txnReconciliations);
            logger.info("Data write to DB successfull");
        }catch(Exception e){
            logger.error("Error writing data to DB : {}", e.getMessage());
            throw new RuntimeException("Error writing data to DB!");
        }

        logger.info("Writing reconciliation results to file");
        fileWriter.writeResults(reconciliationResults, reconciliationFilePath);
    }

    private List<ReconciliationResult> reconcileTransactions(List<Transaction> buyerTransactions , List<Transaction> supplierTransactions) {
        logger.info("Inside reconcileTransactions");
        List<ReconciliationResult> results = new ArrayList<>();

        logger.info("Parsing through buyer transactions for match ");
        for (Transaction buyerTransaction : buyerTransactions) {
            boolean exactMatchFound = false;
            boolean partialMatchFound = false;
            Transaction bestPartialMatch = null;

            for (Transaction supplierTransaction : supplierTransactions) {
                MatchType matchType = transactionMatcher.compareTransactions(buyerTransaction, supplierTransaction);
                switch (matchType) {
                    case EXACT:
                        logger.info("Exact match found with supplier txn having billNo , date and gstIn : {} {} {}", supplierTransaction.getBillNo(),supplierTransaction.getDate(),supplierTransaction.getGstIn());
                        exactMatchFound = true;
                        results.add(new ReconciliationResult(buyerTransaction, supplierTransaction, MatchCategory.EXACT));
                        break;
                    case PARTIAL:
                        logger.debug("Partial match found for buyer txn for billNo , date and gstIn : {} {} {}", buyerTransaction.getBillNo(),buyerTransaction.getDate(),buyerTransaction.getGstIn());
                        partialMatchFound = true;
                        if (bestPartialMatch == null) {
                            bestPartialMatch = supplierTransaction;
                        } else {
                            double currentScore = transactionMatcher.calculateMatchScore(buyerTransaction, supplierTransaction);
                            logger.debug("Current match score : {}", currentScore);
                            double bestScore = transactionMatcher.calculateMatchScore(buyerTransaction, bestPartialMatch);
                            logger.debug("Best match score : {}", bestScore);

                            // Choose the best partial match
                            if (currentScore > bestScore) {
                                bestPartialMatch = supplierTransaction;
                                logger.debug("Best partial match : {} {} {}", supplierTransaction.getBillNo(), supplierTransaction.getGstIn(),supplierTransaction.getDate());
                            }
                        }
                        break;
                    default:
                        // Handle other MatchType values if needed
                        break;
                }
            }

            if (!exactMatchFound) {
                if (partialMatchFound) {
                    logger.info("Exact match found with supplier txn having billNo , date and gstIn : {} {} {}", bestPartialMatch.getBillNo(),bestPartialMatch.getDate(),bestPartialMatch.getGstIn());
                    results.add(new ReconciliationResult(buyerTransaction, bestPartialMatch, MatchCategory.PARTIAL));
                } else {
                    logger.info("Only in buyer case");
                    results.add(new ReconciliationResult(buyerTransaction, null, MatchCategory.ONLY_IN_BUYER));
                }
            }
        }

        // Check for transactions only in supplier
        logger.info("Checking for only in supplier case");
        for (Transaction supplierTransaction : supplierTransactions) {
            boolean found = false;
            for (Transaction buyerTransaction : buyerTransactions) {
                if (transactionMatcher.compareTransactions(buyerTransaction, supplierTransaction) == MatchType.EXACT) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                logger.debug("only in supplier case : supplier txn billNo , gstIn , date : {} {} {}", supplierTransaction.getBillNo(), supplierTransaction.getGstIn(), supplierTransaction.getDate());
                results.add(new ReconciliationResult(null, supplierTransaction, MatchCategory.ONLY_IN_SUPPLIER));
            }
        }

        return results;
    }


}
