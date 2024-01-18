package com.example.TxnReconciliation.service.matchingStrategy;

import com.example.TxnReconciliation.constants.TxnFields;
import com.example.TxnReconciliation.model.Transaction;
import com.example.TxnReconciliation.model.enums.TxnType;
import com.example.TxnReconciliation.service.PropertiesReader;
import com.example.TxnReconciliation.service.fieldMatcher.IFieldMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PartialMatchingStrategyTest {

    @Mock
    private PropertiesReader propertiesReader;

    @Mock
    private IFieldMatcher gstInMatcher;
    @Mock
    private IFieldMatcher dateMatcher;
    @Mock
    private IFieldMatcher billNoMatcher;

    @Mock
    private IFieldMatcher taxableValueMatcher;

    @Mock
    private IFieldMatcher gstRateMatcher;

    @Mock
    private IFieldMatcher igstMatcher;

    @Mock
    private IFieldMatcher cgstMatcher;

    @Mock
    private IFieldMatcher sgstMatcher;

    @Mock
    private IFieldMatcher totalMatcher;

    private PartialMatchingStrategy partialMatchingStrategy;

    @BeforeEach
    void setUp() {
        Map<String, IFieldMatcher> fieldsMatcher = new HashMap<>();
        fieldsMatcher.put(TxnFields.GSTIN, gstInMatcher);
        fieldsMatcher.put(TxnFields.DATE, dateMatcher);
        fieldsMatcher.put(TxnFields.BILLNO, billNoMatcher);
        fieldsMatcher.put(TxnFields.GSTRATE, gstRateMatcher);
        fieldsMatcher.put(TxnFields.TAXABLEVALUE, taxableValueMatcher);
        fieldsMatcher.put(TxnFields.IGST, igstMatcher);
        fieldsMatcher.put(TxnFields.CGST, cgstMatcher);
        fieldsMatcher.put(TxnFields.SGST, sgstMatcher);
        fieldsMatcher.put(TxnFields.TOTAL, totalMatcher);

        when(propertiesReader.getProperty("MATCHING_TXN_FIELDS", "gstIn,date,billNo")).thenReturn("gstIn,date,billNo"); // Set specific fields for tests
        partialMatchingStrategy = new PartialMatchingStrategy();
    }

    @Test
    void match_oneFieldsMatch_returnsTrue() {
        Transaction buyerTransaction = new Transaction("GSTIN1", "2019-04-06", "abc", 4.0, 389.0, 9.0, 1.0, 1.0, 416.0, String.valueOf(TxnType.BUYER));
        Transaction supplierTransaction = new Transaction("GSTIN1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.SUPPLIER));

        when(gstInMatcher.match(anyString(), anyString())).thenReturn(true);
        when(dateMatcher.match(anyString(), anyString())).thenReturn(true);
        when(billNoMatcher.match(anyString(), anyString())).thenReturn(true);

        assertTrue(partialMatchingStrategy.match(buyerTransaction, supplierTransaction));
    }

    @Test
    void match_allFieldsMismatch_returnsFalse() {
        Transaction buyerTransaction = new Transaction("GSTIN1", "2019-04-06", "abc", 4.0, 389.0, 9.0, 1.0, 1.0, 416.0, String.valueOf(TxnType.BUYER));
        Transaction supplierTransaction = new Transaction("abcd1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.SUPPLIER));

        when(gstInMatcher.match(anyString(), anyString())).thenReturn(false);
        when(dateMatcher.match(anyString(), anyString())).thenReturn(true);
        when(billNoMatcher.match(anyString(), anyString())).thenReturn(true);

        assertFalse(partialMatchingStrategy.match(buyerTransaction, supplierTransaction));
    }

    @Test
    void calculateSimilarityScore() {
        Transaction buyerTransaction = new Transaction("GSTIN1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.BUYER));
        Transaction supplierTransaction = new Transaction("GSTIN1", "2022-01-01", "123", 10.0, 100.0, 5.0, 3.0, 2.0, 10.0, String.valueOf(TxnType.SUPPLIER));

        assertEquals(0.78, partialMatchingStrategy.calculateSimilarityScore(buyerTransaction,supplierTransaction));
    }

}
