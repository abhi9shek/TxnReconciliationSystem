package com.example.TxnReconciliation.service;

import com.example.TxnReconciliation.model.Transaction;
import com.example.TxnReconciliation.model.enums.MatchType;
import com.example.TxnReconciliation.model.enums.TxnType;
import com.example.TxnReconciliation.service.matchingStrategy.ExactMatchingStrategy;
import com.example.TxnReconciliation.service.matchingStrategy.PartialMatchingStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionMatcherServiceTest {

    @Mock
    private ExactMatchingStrategy exactMatch;

    @Mock
    private PartialMatchingStrategy partialMatch;

    @InjectMocks
    private TransactionMatcherService service;

    @Test
    void compareTransactions_exactMatch() {
        Transaction buyerTransaction = new Transaction("GSTIN1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.BUYER));
        Transaction supplierTransaction = new Transaction("GSTIN1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.SUPPLIER));

        when(exactMatch.match(buyerTransaction, supplierTransaction)).thenReturn(true);

        MatchType result = service.compareTransactions(buyerTransaction, supplierTransaction);

        assertEquals(MatchType.EXACT, result);
    }

    @Test
    void compareTransactions_partialMatch() {
        Transaction buyerTransaction = new Transaction("GSTIN1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.BUYER));
        Transaction supplierTransaction = new Transaction("GSTIN4", "2022-05-01", "1234", 10.0, 103.0, 7.0, 2.0, 2.0, 15.0, String.valueOf(TxnType.SUPPLIER));

        when(exactMatch.match(buyerTransaction, supplierTransaction)).thenReturn(false);
        when(partialMatch.match(buyerTransaction, supplierTransaction)).thenReturn(true);

        MatchType result = service.compareTransactions(buyerTransaction, supplierTransaction);

        assertEquals(MatchType.PARTIAL, result);
    }

    @Test
    void compareTransactions_noMatch() {
        Transaction buyerTransaction = new Transaction("GSTIN1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.BUYER));
        Transaction supplierTransaction = new Transaction("ABCDFG7", "1998-09-12", "456", 25.0, 210.0, 7.0, 5.0, 8.0, 26.0, String.valueOf(TxnType.SUPPLIER));

        when(exactMatch.match(buyerTransaction, supplierTransaction)).thenReturn(false);
        when(partialMatch.match(buyerTransaction, supplierTransaction)).thenReturn(false);

        MatchType result = service.compareTransactions(buyerTransaction, supplierTransaction);

        assertEquals(MatchType.NONE, result);
    }

    @Test
    void calculateMatchScore() {
        Transaction buyerTransaction = new Transaction("GSTIN1", "01-01-2022", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.BUYER));
        Transaction supplierTransaction = new Transaction("GST4", "03-01-2022", "456", 10.0, 105.0, 5.0, 4.0, 2.0, 12.0, String.valueOf(TxnType.SUPPLIER));
        double expectedScore = 0.72;

        when(partialMatch.calculateSimilarityScore(buyerTransaction, supplierTransaction)).thenReturn(expectedScore);

        double result = service.calculateMatchScore(buyerTransaction, supplierTransaction);

        assertEquals(expectedScore, result);
    }
}
