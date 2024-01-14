package com.example.TxnReconciliation;

import com.example.TxnReconciliation.config.TxnDAOBean;
import com.example.TxnReconciliation.config.TxnReconciliationDAOBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.TxnReconciliation.model"})
@EnableJpaRepositories(basePackages = {"com.example.TxnReconciliation.dao"})
@Import({TxnDAOBean.class, TxnReconciliationDAOBean.class})
public class TxnReconciliationApplication {
    public static void main(String[] args) {

        SpringApplication.run(TxnReconciliationApplication.class, args);

    }
}