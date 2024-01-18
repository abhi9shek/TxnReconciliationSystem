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
class IgstMatcherTest {

    @Mock
    private IFieldTypeMatching fieldTypeMatching;

    @InjectMocks
    private IgstMatcher igstMatcher;

    @Test
    void match_identicalIGSTValues_returnsTrue() {
        String buyerIgst = "9.5";
        String supplierIgst = "9.5";
        when(fieldTypeMatching.match(buyerIgst, supplierIgst)).thenReturn(true);

        assertTrue(igstMatcher.match(buyerIgst, supplierIgst));
    }

    @Test
    void match_differentIGSTValues_returnsFalse() {
        String buyerIgst = "9.5";
        String supplierIgst = "18.5";
        when(fieldTypeMatching.match(buyerIgst, supplierIgst)).thenReturn(false);

        assertFalse(igstMatcher.match(buyerIgst, supplierIgst));
    }

    @Test
    void match_handlesNonNumericValues_returnsFalse() {
        String buyerIgst = "invalid";
        String supplierIgst = "9.5";

        assertFalse(igstMatcher.match(buyerIgst, supplierIgst));
    }

    @Test
    void similarityScore_calculatesCorrectScore() {
        String value1 = "9.5";
        String value2 = "10.0";
        double expectedScore = 0.95;
        when(fieldTypeMatching.getSimilarityScore(value1, value2)).thenReturn(expectedScore);

        double score = igstMatcher.getSimilarityScore(value1, value2);
        assertEquals(expectedScore, score);
    }
}
