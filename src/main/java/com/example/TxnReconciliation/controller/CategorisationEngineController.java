package com.example.TxnReconciliation.controller;

import com.example.TxnReconciliation.dataTransferObject.TxnCategorisationRequest;
import com.example.TxnReconciliation.service.CategorisationEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/categorisation-engine/v1/")
public class CategorisationEngineController {

    static final Logger logger = LoggerFactory.getLogger(String.valueOf(CategorisationEngineController.class));

    private final CategorisationEngineService categorisationEngine;
    public CategorisationEngineController(CategorisationEngineService categorisationEngine) {
        this.categorisationEngine = categorisationEngine;
    }

    @RequestMapping(
            value = "reconcile",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    ResponseEntity<String> reconcileTxns(
            @RequestBody TxnCategorisationRequest request) {

        try {
            categorisationEngine.processReconciliationResults(request.getBuyerTxnsFilePath(), request.getSupplierTxnsFilePath(), request.getReconciliationFilePath());
            return new ResponseEntity<>("Transactions reconciled successfully!", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Reconciliation failed due to : {}", e.getMessage());

            return new ResponseEntity<>("Error reconciling transactions: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
