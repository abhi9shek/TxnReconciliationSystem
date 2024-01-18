package com.example.TxnReconciliation.service.fieldTypeMatcher;

import com.example.TxnReconciliation.service.PropertiesReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StringMatchingTest {

    @Mock
    private PropertiesReader propertiesReader;

    @Mock
    private com.example.TxnReconciliation.utils.StringMatching stringMatchingUtil;

    private StringMatching stringMatching;

    @BeforeEach
    void setUp() {
        when(propertiesReader.getProperty("STRING_THRESHOLD", "0.8")).thenReturn("0.75"); // Set a specific threshold for tests
        stringMatching = new StringMatching(); // Inject dependencies
    }

    @Test
    void match_identicalStrings_returnsTrue() {
        String value1 = "hello";
        String value2 = "hello";

        assertTrue(stringMatching.match(value1, value2));
    }

    @Test
    void match_similarStringsWithinThreshold_returnsTrue() {
        String value1 = "hello";
        String value2 = "hxllo";
        when(stringMatchingUtil.getDistanceBetweenStrings(value1, value2)).thenReturn(1);

        assertTrue(stringMatching.match(value1, value2));
    }

    @Test
    void match_dissimilarStringsBeyondThreshold_returnsFalse() {
        String value1 = "hello";
        String value2 = "world";
        when(stringMatchingUtil.getDistanceBetweenStrings(value1, value2)).thenReturn(4);

        assertFalse(stringMatching.match(value1, value2));
    }

    @Test
    void similarityScore_calculatesCorrectScore() {
        String value1 = "kity";
        String value2 = "sity";
        when(stringMatchingUtil.getDistanceBetweenStrings(value1, value2)).thenReturn(3);

        double score = stringMatching.getSimilarityScore(value1, value2);
        assertEquals(0.75, score);
    }
}
