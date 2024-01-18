package com.example.TxnReconciliation.dao;

import com.example.TxnReconciliation.dataTransferObject.ReconciliationResult;
import com.example.TxnReconciliation.model.TxnReconciliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TxnReconciliationDAO extends JpaRepository<TxnReconciliation,Long> {

}
