package com.example.TxnReconciliation.service.fileReader;

import com.example.TxnReconciliation.model.Transaction;
import com.example.TxnReconciliation.model.enums.TxnType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CSVFileReaderTest {

    @InjectMocks
    private CSVFileReader csvFileReader;

    @Test
    void readTransactions_validCSVFile_returnsTransactions() {

        // Call the method to test
        List<Transaction> transactions = csvFileReader.readTransactions("/home/abhishek/TxnFiles/TestCasesFiles/testBuyer.csv", String.valueOf(TxnType.BUYER));

        // Verify expected results
        assertEquals(2, transactions.size());
        Transaction transaction1 = transactions.get(0);
        assertEquals("1234567890", transaction1.getGstIn());
        assertEquals("2023-01-15", transaction1.getDate());
        assertEquals("INV-101", transaction1.getBillNo());
        assertEquals(18.0, transaction1.getGstRate());
        assertEquals(1000.00, transaction1.getTaxableValue());
        assertEquals(180.0, transaction1.getIgst());
        assertEquals(90.0, transaction1.getCgst());
        assertEquals(90.0, transaction1.getSgst());
        assertEquals(1180.0, transaction1.getTotal());
        assertEquals("BUYER", transaction1.getTxnType());
    }

    @Test
    void readTransactions_missingHeaders_throwsException() {

        assertThrows(IOException.class, () -> csvFileReader.readTransactions("/home/abhishek/TxnFiles/TestCasesFiles/testMissingHeaders.csv", String.valueOf(TxnType.BUYER)));
    }

    @Test
    void readTransactions_duplicateTransactions_skipsDuplicates() throws IOException {

        List<Transaction> transactions = csvFileReader.readTransactions("/home/abhishek/TxnFiles/TestCasesFiles/testDuplicates.csv", String.valueOf(TxnType.BUYER));

        assertEquals(2, transactions.size()); // Only the unique transactions are returned
    }
}
