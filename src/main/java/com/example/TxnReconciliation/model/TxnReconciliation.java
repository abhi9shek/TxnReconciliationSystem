package com.example.TxnReconciliation.model;

import com.example.TxnReconciliation.model.enums.MatchCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
@Table(name = "txn_reconciliation")
public class TxnReconciliation {

    @JsonProperty(value = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "buyer_txn_id" , referencedColumnName = "id")
    private Transaction buyerTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "supplier_txn_id" , referencedColumnName = "id")
    private Transaction supplierTransaction;

    @Enumerated(EnumType.STRING)
    private MatchCategory matchCategory;

    public TxnReconciliation(Transaction buyerTransaction, Transaction supplierTransaction, MatchCategory matchCategory) {
        this.buyerTransaction = buyerTransaction;
        this.supplierTransaction = supplierTransaction;
        this.matchCategory = matchCategory;
    }

}

