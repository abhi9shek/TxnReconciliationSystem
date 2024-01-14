package com.example.TxnReconciliation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example.TxnReconciliation.dao") // Specify the package containing TxnDAO
public class TxnDAOBean {
}
