package com.example.TxnReconciliation.service.matchingStrategy;

import com.example.TxnReconciliation.model.Transaction;
import com.example.TxnReconciliation.constants.TxnFields;
import com.example.TxnReconciliation.service.PropertiesReader;
import com.example.TxnReconciliation.service.fieldMatcher.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PartialMatchingStrategy implements IMatchingStrategy {

    private static final String TXN_FIELDS;
    private static final String TXN_FIELD_WEIGHTS;

    static {
        TXN_FIELDS = PropertiesReader.getInstance().getProperty("MATCHING_TXN_FIELDS", "gstIn,date,billNo,taxableValue,gstRate,igst,cgst,sgst,total");
        TXN_FIELD_WEIGHTS = PropertiesReader.getInstance().getProperty("TXN_FIELDS_WEIGHTS", "gstIn-0.3,date-0.2,billNo-0.3,taxableValue-0.1,gstRate-0.1,igst-0.05,cgst-0.05,sgst-0.05,total-0.05");
    }

    private final HashMap<String, IFieldMatcher> fieldsMatcher = new HashMap<>();
    private final HashMap<String, Function<Transaction,?>> fieldExtractors = new HashMap<>();

//    double[] weights = {0.3, 0.2, 0.3, 0.1, 0.1, 0.05, 0.05, 0.05, 0.05};
//    String[] fields = {TxnFields.GSTIN, TxnFields.DATE, TxnFields.BILLNO, TxnFields.TAXABLEVALUE,
//            TxnFields.GSTRATE, TxnFields.IGST, TxnFields.CGST, TxnFields.SGST, TxnFields.TOTAL};

    String[] fields = TXN_FIELDS.split(",");
    String[] weightPairs = TXN_FIELD_WEIGHTS.split(",");

    public PartialMatchingStrategy() {
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

            if (fieldMatcher.match(String.valueOf(fieldValue1),String.valueOf(fieldValue2))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double calculateSimilarityScore(Transaction txn1, Transaction txn2) {
        double weightedAverage = 0.0;
        double sumOfWeights = 0.0;

        HashMap<String,Double> fieldWeights = new HashMap<>();
        for (String weightPair : weightPairs) {
            String[] parts = weightPair.split("-");
            String field = parts[0];
            double weight = Double.parseDouble(parts[1]);

            // Match fields by key
            for (String matchedField : fields) {
                if (field.equals(matchedField)) {
                    fieldWeights.put(field, weight);
                    break;
                }
            }
        }


        for (Map.Entry<String, Double> entry : fieldWeights.entrySet()) {
            String field = entry.getKey();
            double weight = entry.getValue();

            IFieldMatcher fieldMatcher = fieldsMatcher.get(field);

            // Get the field values using the provided extractor function
            Object fieldValue1 = fieldExtractors.get(field).apply(txn1);
            Object fieldValue2 = fieldExtractors.get(field).apply(txn2);

            // Calculate similarity score for the current field
            double similarityScore = fieldMatcher.similarityScore(String.valueOf(fieldValue1), String.valueOf(fieldValue2));

            // Update the weighted average
            weightedAverage += weight * similarityScore;
            sumOfWeights += weight;
        }

        // Calculate the final weighted average
        if (sumOfWeights > 0) {
            weightedAverage /= sumOfWeights;
        } else {
            // Handle the case where totalWeight is zero to avoid division by zero
            weightedAverage = 0.0;
        }

        weightedAverage = BigDecimal.valueOf(weightedAverage).setScale(2, RoundingMode.HALF_UP).doubleValue();

        return weightedAverage;
    }
}
