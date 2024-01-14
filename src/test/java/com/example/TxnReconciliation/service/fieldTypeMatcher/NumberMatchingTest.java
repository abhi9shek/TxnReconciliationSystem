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
class NumberMatchingTest {

    @Mock
    private PropertiesReader propertiesReader;

    private NumberMatching numberMatching;

    @BeforeEach
    void setUp() {
        when(propertiesReader.getProperty("NUMBER_THRESHOLD", "0.9")).thenReturn("0.85"); // Set a specific threshold for tests
        numberMatching = new NumberMatching(); // Inject dependency
    }

    @Test
    void match_identicalNumbers_returnsTrue() {
        String value1 = "10.5";
        String value2 = "10.5";

        assertTrue(numberMatching.match(value1, value2));
    }

    @Test
    void match_similarNumbersWithinThreshold_returnsTrue() {
        String value1 = "12.34";
        String value2 = "12.5";

        assertTrue(numberMatching.match(value1, value2));
    }

    @Test
    void match_dissimilarNumbersBeyondThreshold_returnsFalse() {
        String value1 = "50";
        String value2 = "20";

        assertFalse(numberMatching.match(value1, value2));
    }

    @Test
    void match_handlesNumberFormatException_returnsFalse() {
        String value1 = "hello";
        String value2 = "10";

        assertFalse(numberMatching.match(value1, value2));
    }

    @Test
    void similarityScore_calculatesCorrectScore() {
        String value1 = "100";
        String value2 = "85";

        double score = numberMatching.similarityScore(value1, value2);
        assertEquals(0.85, score, 0.000001); // Allow slight tolerance for floating-point comparison
    }
}
