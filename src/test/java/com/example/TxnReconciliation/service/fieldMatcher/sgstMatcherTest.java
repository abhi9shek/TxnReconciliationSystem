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
class sgstMatcherTest {

    @Mock
    private IFieldTypeMatching fieldTypeMatching;

    @InjectMocks
    private sgstMatcher sgstMatcher;

    @Test
    void match_identicalSGSTValues_returnsTrue() {
        String buyerSgst = "9.5";
        String supplierSgst = "9.5";
        when(fieldTypeMatching.match(buyerSgst, supplierSgst)).thenReturn(true);

        assertTrue(sgstMatcher.match(buyerSgst, supplierSgst));
    }

    @Test
    void match_differentSGSTValues_returnsFalse() {
        String buyerSgst = "9.5";
        String supplierSgst = "18.0";
        when(fieldTypeMatching.match(buyerSgst, supplierSgst)).thenReturn(false);

        assertFalse(sgstMatcher.match(buyerSgst, supplierSgst));
    }

    @Test
    void match_handlesNonNumericValues_returnsFalse() {
        String buyerSgst = "invalid";
        String supplierSgst = "18.0";
        assertFalse(sgstMatcher.match(buyerSgst, supplierSgst));
    }

    @Test
    void similarityScore_calculatesCorrectScore() {
        String value1 = "9.5";
        String value2 = "10.0";
        double expectedScore = 0.95;
        when(fieldTypeMatching.similarityScore(value1, value2)).thenReturn(expectedScore);

        double score = sgstMatcher.similarityScore(value1, value2);
        assertEquals(expectedScore, score);
    }
}
