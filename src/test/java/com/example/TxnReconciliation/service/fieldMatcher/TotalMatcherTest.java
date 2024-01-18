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
class TotalMatcherTest {

    @Mock
    private IFieldTypeMatching fieldTypeMatching;

    @InjectMocks
    private TotalMatcher totalMatcher;

    @Test
    void match_identicaltotals_returnsTrue() {
        String buyertotal = "10000.00";
        String suppliertotal = "10000.00";
        when(fieldTypeMatching.match(buyertotal, suppliertotal)).thenReturn(true);

        assertTrue(totalMatcher.match(buyertotal, suppliertotal));
    }

    @Test
    void match_differenttotals_returnsFalse() {
        String buyertotal = "10000.00";
        String suppliertotal = "5000.00";
        when(fieldTypeMatching.match(buyertotal, suppliertotal)).thenReturn(false);

        assertFalse(totalMatcher.match(buyertotal, suppliertotal));
    }

    @Test
    void match_handlesNonNumericValues_returnsFalse() {
        String buyertotal = "invalid";
        String suppliertotal = "10000.00";
        assertFalse(totalMatcher.match(buyertotal, suppliertotal));
    }

    @Test
    void similarityScore_calculatesCorrectScore() {
        String value1 = "10000.00";
        String value2 = "11000.00";
        double expectedScore = 0.9090909090909091;
        when(fieldTypeMatching.getSimilarityScore(value1, value2)).thenReturn(expectedScore);

        double score = totalMatcher.getSimilarityScore(value1, value2);
        assertEquals(expectedScore, score);
    }

    @Test
    void match_handlesEmptyValues_returnsFalse() {
        assertFalse(totalMatcher.match("", ""));
        assertFalse(totalMatcher.match("", "10000.00"));
        assertFalse(totalMatcher.match("10000.00", ""));
    }

    @Test
    void match_handlesZerototal_returnsTrueForIdenticalZeros() {
        assertTrue(totalMatcher.match("0", "0"));
    }

    @Test
    void match_handlesInvalidtotalFormats_returnsFalse() {
        assertFalse(totalMatcher.match("abc", "10000.00"));
        assertFalse(totalMatcher.match("10K", "10000.00"));
    }

    @Test
    void match_handlesBoundaryValues_returnsCorrectResults() {
        assertTrue(totalMatcher.match("0.00", "0.00"));
    }
}
