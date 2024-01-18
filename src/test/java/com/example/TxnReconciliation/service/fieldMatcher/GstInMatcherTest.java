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
class GstInMatcherTest {

    @Mock
    private IFieldTypeMatching fieldTypeMatching;

    @InjectMocks
    private GstInMatcher gstInMatcher;

    @Test
    void match_identicalGSTINs_returnsTrue() {
        String buyerGstIn = "29AACCE4567F1Z5";
        String supplierGstIn = "29AACCE4567F1Z5";
        when(fieldTypeMatching.match(buyerGstIn, supplierGstIn)).thenReturn(true);

        assertTrue(gstInMatcher.match(buyerGstIn, supplierGstIn));
    }

    @Test
    void match_differentGSTINs_returnsFalse() {
        String buyerGstIn = "29AACCE4567F1Z5";
        String supplierGstIn = "35AACCE89012F1Z4";
        when(fieldTypeMatching.match(buyerGstIn, supplierGstIn)).thenReturn(false);

        assertFalse(gstInMatcher.match(buyerGstIn, supplierGstIn));
    }

    @Test
    void similarityScore_calculatesCorrectScore() {
        String value1 = "29AACCE4567F1Z5";
        String value2 = "27AACCE1234F1Z7";
        double expectedScore = 0.6;
        when(fieldTypeMatching.getSimilarityScore(value1, value2)).thenReturn(expectedScore);

        double score = gstInMatcher.getSimilarityScore(value1, value2);
        assertEquals(expectedScore, score);
    }
}
