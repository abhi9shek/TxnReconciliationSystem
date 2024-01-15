package com.example.TxnReconciliation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;


@Entity
@Table(name = "transactions")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    @JsonProperty(value = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(value = "gstin")
    @Column(name = "gst_in")
    private String gstIn;

    @JsonProperty(value = "date")
    private String date;

    @JsonProperty(value = "billno")
    private String billNo;

    @JsonProperty(value = "gstrate")
    @Column(name = "gst_rate")
    private Double gstRate;

    @JsonProperty(value = "taxablevalue")
    @Column(name = "taxable_value")
    private Double taxableValue;

    @JsonProperty(value = "igst")
    private Double igst;

    @JsonProperty(value = "cgst")
    private Double cgst;

    @JsonProperty(value = "sgst")
    private Double sgst;

    @JsonProperty(value = "txntype")
    @Column(name = "txn_type")
    private String txnType;

    @JsonProperty(value = "total")
    private Double total;

    public Transaction(){
    }

    public Transaction(String gstIn,String date,String billNo,Double gstRate,Double taxableValue,Double igst,Double cgst,Double sgst,Double total,String txnType) {
        this.gstIn = gstIn;
        this.date = date;
        this.billNo = billNo;
        this.gstRate = gstRate;
        this.taxableValue = taxableValue;
        this.igst = igst;
        this.cgst = cgst;
        this.sgst = sgst;
        this.total = total;
        this.txnType = txnType;
    }

    public String getGstIn() {
        return gstIn;
    }

    public String getDate() {
        return date;
    }

    public String getBillNo() {
        return billNo;
    }

    public Double getGstRate() {
        return gstRate;
    }

    public Double getTaxableValue() {
        return taxableValue;
    }

    public Double getIgst() {
        return igst;
    }

    public Double getCgst() {
        return cgst;
    }

    public Double getSgst() {
        return sgst;
    }

    public Double getTotal() {
        return total;
    }

    public String getTxnType() {
        return txnType;
    }
}
