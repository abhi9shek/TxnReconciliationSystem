package com.example.TxnReconciliation.dao.mapper;

import com.example.TxnReconciliation.dataTransferObject.ReconciliationResult;
import com.example.TxnReconciliation.model.TxnReconciliation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReconciliationResMapper {

    public List<TxnReconciliation> reconciliationResultToTxnReconciliationMapper(List<ReconciliationResult> reconciliationResults){
        List<TxnReconciliation> txnReconciliations = new ArrayList<>();
        for(ReconciliationResult reconciliationResult: reconciliationResults){
            TxnReconciliation txnReconciliation = new TxnReconciliation(reconciliationResult.getBuyerTransaction(), reconciliationResult.getSupplierTransaction(), reconciliationResult.getMatchCategory());
            txnReconciliations.add(txnReconciliation);
        }
        return txnReconciliations;
    }


}
