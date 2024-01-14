package com.example.TxnReconciliation.dao;

import com.example.TxnReconciliation.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TxnDAO extends JpaRepository<Transaction, Long> {

    //implement custom methods

}
