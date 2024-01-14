package com.example.TxnReconciliation.service.fieldTypeMatcher;

import com.example.TxnReconciliation.service.PropertiesReader;
import com.example.TxnReconciliation.utils.DateConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Date;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DateMatchingTest {

    @Mock
    private PropertiesReader propertiesReader;

    private DateMatching dateMatching;

    @BeforeEach
    void setUp() {
        when(propertiesReader.getProperty("DATE_THRESHOLD", "1")).thenReturn("2"); // Set a specific threshold for tests
        dateMatching = new DateMatching(); // Inject dependencies
    }

    @Test
    void match_identicalDates_returnsTrue() throws ParseException {
        String value1 = "2023-11-21";
        String value2 = "2023-11-21";

        assertTrue(dateMatching.match(value1, value2));
    }

    @Test
    void match_datesWithinThreshold_returnsTrue() throws ParseException {
        String value1 = "2023-11-20";
        String value2 = "2023-11-20";

        assertTrue(dateMatching.match(value1, value2));
    }

    @Test
    void match_datesBeyondThreshold_returnsFalse() throws ParseException {
        String value1 = "2023-11-15";
        String value2 = "2023-11-25";

        assertFalse(dateMatching.match(value1, value2));
    }


    @Test
    void similarityScore_calculatesCorrectScore() throws ParseException {
        String value1 = "2023-11-18";
        String value2 = "2023-11-23";

        double score = dateMatching.similarityScore(value1, value2);
        assertEquals(5, score);
    }
}
