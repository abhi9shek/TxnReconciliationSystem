package com.example.TxnReconciliation.service.fieldMatcher;

import com.example.TxnReconciliation.service.fieldTypeMatcher.IFieldTypeMatching;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class billNoMatcherTest {

    @Mock
    private IFieldTypeMatching fieldTypeMatching;

    @InjectMocks
    private billNoMatcher billNoMatcher;

    @Test
    void match_identicalBillNos_returnsTrue() {
        String buyerBillNo = "12345";
        String supplierBillNo = "12345";
        when(fieldTypeMatching.match(buyerBillNo, supplierBillNo)).thenReturn(true);

        assertTrue(billNoMatcher.match(buyerBillNo, supplierBillNo));
    }

    @Test
    void match_differentBillNos_returnsFalse() {
        String buyerBillNo = "12345";
        String supplierBillNo = "67890";
        when(fieldTypeMatching.match(buyerBillNo, supplierBillNo)).thenReturn(false);

        assertFalse(billNoMatcher.match(buyerBillNo, supplierBillNo));
    }

    @Test
    void similarityScore_calculatesCorrectScore() {
        String value1 = "ABC123";
        String value2 = "ABC456";
        double expectedScore = 0.5;
        when(fieldTypeMatching.similarityScore(value1, value2)).thenReturn(expectedScore);

        double score = billNoMatcher.similarityScore(value1, value2);
        assertEquals(expectedScore, score);
    }
}
