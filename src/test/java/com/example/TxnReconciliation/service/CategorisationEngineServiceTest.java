package com.example.TxnReconciliation.service;

import com.example.TxnReconciliation.dao.TxnDAO;
import com.example.TxnReconciliation.dao.TxnReconciliationDAO;
import com.example.TxnReconciliation.dao.mapper.ReconciliationResMapper;
import com.example.TxnReconciliation.model.Transaction;
import com.example.TxnReconciliation.model.TxnReconciliation;
import com.example.TxnReconciliation.model.enums.MatchCategory;
import com.example.TxnReconciliation.dataTransferObject.ReconciliationResult;
import com.example.TxnReconciliation.model.enums.MatchType;
import com.example.TxnReconciliation.model.enums.TxnType;
import com.example.TxnReconciliation.service.fileFactory.CSVFileFactory;
import com.example.TxnReconciliation.service.fileFactory.IFileFactory;
import com.example.TxnReconciliation.service.fileReader.IFileReader;
import com.example.TxnReconciliation.service.fileWriter.IFileWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class CategorisationEngineServiceTest {

    @Mock
    private TxnDAO txnDAO;

    @Mock
    private TxnReconciliationDAO txnReconciliationDAO;

    @Mock
    private ReconciliationResMapper reconciliationResMapper;

    @InjectMocks
    private CategorisationEngineService categorisationEngineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processReconciliationResults_Success() {
        // Arrange
        when(txnDAO.findAll()).thenReturn(Arrays.asList(
                new Transaction("GSTIN1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.BUYER)),
                new Transaction("GSTIN2", "2022-01-02", "456", 8.0, 80.0, 4.0, 2.0, 2.0, 8.0, String.valueOf(TxnType.SUPPLIER))
        ));

        List<Transaction> buyerTransactions = Arrays.asList(
                new Transaction("GSTIN3", "2022-01-03", "789", 12.0, 120.0, 6.0, 3.0, 3.0, 12.0, String.valueOf(TxnType.BUYER)),
                new Transaction("GSTIN1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.BUYER))
        );

        List<Transaction> supplierTransactions = Arrays.asList(
                new Transaction("GSTIN3", "2022-01-03", "789", 12.0, 120.0, 6.0, 3.0, 3.0, 12.0, String.valueOf(TxnType.SUPPLIER)),
                new Transaction("GSTIN2", "2022-01-01", "123", 8.0, 80.0, 4.0, 2.0, 2.0, 8.0, String.valueOf(TxnType.SUPPLIER))
        );

        when(reconciliationResMapper.reconciliationResultToTxnReconciliationMapper(anyList())).thenReturn(Arrays.asList(
                new TxnReconciliation(buyerTransactions.get(0), supplierTransactions.get(0),  MatchCategory.EXACT),
                new TxnReconciliation(buyerTransactions.get(1), supplierTransactions.get(1),  MatchCategory.PARTIAL)
        ));

        // Act
        categorisationEngineService.processReconciliationResults("/home/abhishek/TxnFiles/Buyer.csv", "/home/abhishek/TxnFiles/Supplier.csv", "/home/abhishek/TxnFiles/Res.csv");

        // Assert
        verify(txnDAO, times(2)).saveAll(anyList());
        verify(txnReconciliationDAO).saveAll(anyList());
    }

    @Test
    void validationsOnTxns_SkipsExistingTransaction() {
        // Arrange
        when(txnDAO.findAll()).thenReturn(Arrays.asList(
                new Transaction("GSTIN1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.BUYER))
        ));

        List<Transaction> transactions = Arrays.asList(
                new Transaction("GSTIN1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.BUYER)),
                new Transaction("GSTIN2", "2022-01-02", "456", 8.0, 80.0, 4.0, 2.0, 2.0, 8.0, String.valueOf(TxnType.BUYER))
        );

        // Act
        List<Transaction> result = categorisationEngineService.validationsOnTxns(transactions);

        // Assert
        verify(txnDAO).findAll();
        assertEquals(1, result.size());
        assertEquals("GSTIN2", result.get(0).getGstIn());
    }

}
