package com.boti.productmanagerapp.adapters.out.batchprocess;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExecutorConfig {

    private static int ingestionPoolSize;

    @Value("${app.ingestion.pool-size}")
    public void setIngestionPoolSize(int poolSize) {
        ExecutorConfig.ingestionPoolSize = poolSize;
    }

    public static int getIngestionPoolSize() {
        return ingestionPoolSize;
    }
}