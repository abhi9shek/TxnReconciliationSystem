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
class cgstMatcherTest {

    @Mock
    private IFieldTypeMatching fieldTypeMatching;

    @InjectMocks
    private cgstMatcher cgstMatcher;

    @Test
    void match_identicalCGSTValues_returnsTrue() {
        String buyerCgst = "9.5";
        String supplierCgst = "9.5";
        when(fieldTypeMatching.match(buyerCgst, supplierCgst)).thenReturn(true);

        assertTrue(cgstMatcher.match(buyerCgst, supplierCgst));
    }

    @Test
    void match_differentCGSTValues_returnsFalse() {
        String buyerCgst = "9.5";
        String supplierCgst = "18.0";
        when(fieldTypeMatching.match(buyerCgst, supplierCgst)).thenReturn(false);

        assertFalse(cgstMatcher.match(buyerCgst, supplierCgst));
    }

    @Test
    void match_handlesNonNumericValues_returnsFalse() {
        String buyerCgst = "invalid";
        String supplierCgst = "18.0";
        assertFalse(cgstMatcher.match(buyerCgst, supplierCgst));
    }

    @Test
    void similarityScore_calculatesCorrectScore() {
        String value1 = "9.5";
        String value2 = "10.0";
        double expectedScore = 0.95;
        when(fieldTypeMatching.similarityScore(value1, value2)).thenReturn(expectedScore);

        double score = cgstMatcher.similarityScore(value1, value2);
        assertEquals(expectedScore, score);
    }
}
