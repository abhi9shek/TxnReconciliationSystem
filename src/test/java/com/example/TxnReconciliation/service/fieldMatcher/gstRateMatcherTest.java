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
class gstRateMatcherTest {

    @Mock
    private IFieldTypeMatching fieldTypeMatching;

    @InjectMocks
    private gstRateMatcher gstRateMatcher;

    @Test
    void match_identicalGSTRates_returnsTrue() {
        String buyerGstRate = "18.0";
        String supplierGstRate = "18.0";
        when(fieldTypeMatching.match(buyerGstRate, supplierGstRate)).thenReturn(true);

        assertTrue(gstRateMatcher.match(buyerGstRate, supplierGstRate));
    }

    @Test
    void match_differentGSTRates_returnsFalse() {
        String buyerGstRate = "18.0";
        String supplierGstRate = "5.0";
        when(fieldTypeMatching.match(buyerGstRate, supplierGstRate)).thenReturn(false);

        assertFalse(gstRateMatcher.match(buyerGstRate, supplierGstRate));
    }

    @Test
    void match_handlesNonNumericValues_returnsFalse() {
        String buyerGstRate = "invalid";
        String supplierGstRate = "18.0";
        assertFalse(gstRateMatcher.match(buyerGstRate, supplierGstRate)); // Assuming NumberMatching handles non-numerics
    }

    @Test
    void similarityScore_calculatesCorrectScore() {
        String value1 = "18.0";
        String value2 = "20.0"; // Different but close GST rates
        double expectedScore = 0.9;  // Assuming a hypothetical similarity calculation
        when(fieldTypeMatching.similarityScore(value1, value2)).thenReturn(expectedScore);

        double score = gstRateMatcher.similarityScore(value1, value2);
        assertEquals(expectedScore, score);
    }

    @Test
    void match_handlesEmptyValues_returnsFalse() {
        assertFalse(gstRateMatcher.match("", ""));
        assertFalse(gstRateMatcher.match("", "18.0"));
        assertFalse(gstRateMatcher.match("18.0", ""));
    }

    @Test
    void match_handlesZeroGSTRate_returnsTrueForIdenticalZeros() {
        assertTrue(gstRateMatcher.match("0", "0"));
    }

    @Test
    void match_handlesInvalidGSTRateFormats_returnsFalse() {
        assertFalse(gstRateMatcher.match("abc", "18.0"));
        assertFalse(gstRateMatcher.match("18%", "18.0"));
    }

    @Test
    void match_handlesBoundaryValues_returnsCorrectResults() {
        assertTrue(gstRateMatcher.match("0.0", "0.0"));
        assertTrue(gstRateMatcher.match("28.0", "28.0"));
        assertFalse(gstRateMatcher.match("0.0", "0.1"));
    }
}
