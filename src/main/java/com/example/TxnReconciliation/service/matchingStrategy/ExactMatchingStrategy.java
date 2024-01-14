package com.example.TxnReconciliation.service.matchingStrategy;

import com.example.TxnReconciliation.model.Transaction;
import com.example.TxnReconciliation.constants.TxnFields;
import com.example.TxnReconciliation.service.PropertiesReader;
import com.example.TxnReconciliation.service.fieldMatcher.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.function.Function;

public class ExactMatchingStrategy implements IMatchingStrategy {

    static final Logger logger = LoggerFactory.getLogger(String.valueOf(ExactMatchingStrategy.class));

    private static final String TXN_FIELDS;

    static {
        TXN_FIELDS = PropertiesReader.getInstance().getProperty("MATCHING_TXN_FIELDS", "gstIn,date,billNo,taxableValue,gstRate,igst,cgst,sgst,total");
    }


    private final HashMap<String, IFieldMatcher> fieldsMatcher = new HashMap<>();
    private final HashMap<String, Function<Transaction,?>> fieldExtractors = new HashMap<>();

    String[] fields = TXN_FIELDS.split(",");
    public ExactMatchingStrategy() {
        fieldsMatcher.put(TxnFields.GSTIN, new gstInMatcher());
        fieldsMatcher.put(TxnFields.DATE, new dateMatcher());
        fieldsMatcher.put(TxnFields.BILLNO, new billNoMatcher());
        fieldsMatcher.put(TxnFields.TAXABLEVALUE, new taxableValueMatcher());
        fieldsMatcher.put(TxnFields.GSTRATE, new gstRateMatcher());
        fieldsMatcher.put(TxnFields.IGST, new igstMatcher());
        fieldsMatcher.put(TxnFields.CGST, new cgstMatcher());
        fieldsMatcher.put(TxnFields.SGST, new sgstMatcher());
        fieldsMatcher.put(TxnFields.TOTAL, new totalMatcher());

        fieldExtractors.put(TxnFields.GSTIN, Transaction::getGstIn);
        fieldExtractors.put(TxnFields.DATE, Transaction::getDate);
        fieldExtractors.put(TxnFields.BILLNO, Transaction::getBillNo);
        fieldExtractors.put(TxnFields.TAXABLEVALUE, Transaction::getTaxableValue);
        fieldExtractors.put(TxnFields.GSTRATE, Transaction::getGstRate);
        fieldExtractors.put(TxnFields.IGST, Transaction::getIgst);
        fieldExtractors.put(TxnFields.CGST, Transaction::getCgst);
        fieldExtractors.put(TxnFields.SGST, Transaction::getSgst);
        fieldExtractors.put(TxnFields.TOTAL, Transaction::getTotal);
    }



    @Override
    public boolean match(Transaction txn1, Transaction txn2 ) {
        for (String field : fields) {
            // Get the field matcher strategy for the field
            IFieldMatcher fieldMatcher = fieldsMatcher.get(field);

            // Get the field values using the provided extractor function
            Object fieldValue1 = fieldExtractors.get(field).apply(txn1);
            Object fieldValue2 = fieldExtractors.get(field).apply(txn2);

            if (!fieldMatcher.match(String.valueOf(fieldValue1),String.valueOf(fieldValue2))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double calculateSimilarityScore(Transaction txn1, Transaction txn2) {
        return 1;
    }

}
