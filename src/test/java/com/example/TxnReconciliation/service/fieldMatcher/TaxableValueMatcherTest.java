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
class TaxableValueMatcherTest {

    @Mock
    private IFieldTypeMatching fieldTypeMatching;

    @InjectMocks
    private TaxableValueMatcher taxableValueMatcher;

    @Test
    void match_identicalTaxableValues_returnsTrue() {
        String buyerTaxableValue = "10000.00";
        String supplierTaxableValue = "10000.00";
        when(fieldTypeMatching.match(buyerTaxableValue, supplierTaxableValue)).thenReturn(true);

        assertTrue(taxableValueMatcher.match(buyerTaxableValue, supplierTaxableValue));
    }

    @Test
    void match_differentTaxableValues_returnsFalse() {
        String buyerTaxableValue = "10000.00";
        String supplierTaxableValue = "5000.00";
        when(fieldTypeMatching.match(buyerTaxableValue, supplierTaxableValue)).thenReturn(false);

        assertFalse(taxableValueMatcher.match(buyerTaxableValue, supplierTaxableValue));
    }

    @Test
    void match_handlesNonNumericValues_returnsFalse() {
        String buyerTaxableValue = "random";
        String supplierTaxableValue = "10000.00";
        assertFalse(taxableValueMatcher.match(buyerTaxableValue, supplierTaxableValue));
    }

    @Test
    void similarityScore_calculatesCorrectScore() {
        String value1 = "10000.00";
        String value2 = "11000.00";
        double expectedScore = 0.9090909090909091;
        when(fieldTypeMatching.getSimilarityScore(value1, value2)).thenReturn(expectedScore);

        double score = taxableValueMatcher.getSimilarityScore(value1, value2);
        assertEquals(expectedScore, score);
    }


    @Test
    void match_handlesEmptyValues_returnsFalse() {
        assertFalse(taxableValueMatcher.match("", ""));
        assertFalse(taxableValueMatcher.match("", "10000.00"));
        assertFalse(taxableValueMatcher.match("10000.00", ""));
    }

    @Test
    void match_handlesZeroTaxableValue_returnsTrueForIdenticalZeros() {
        assertTrue(taxableValueMatcher.match("0", "0"));
    }

    @Test
    void match_handlesInvalidTaxableValueFormats_returnsFalse() {
        assertFalse(taxableValueMatcher.match("abc", "10000.00"));
        assertFalse(taxableValueMatcher.match("10K", "10000.00"));
    }

    @Test
    void match_handlesBoundaryValues_returnsCorrectResults() {
        assertTrue(taxableValueMatcher.match("0.00", "0.00"));
    }
}
