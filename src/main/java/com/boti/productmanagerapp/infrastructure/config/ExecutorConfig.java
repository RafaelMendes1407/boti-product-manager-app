package com.boti.productmanagerapp.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Bean(name = "fileReaderExecutor", destroyMethod = "shutdown")
    public ExecutorService fileReaderExecutor() {
        int poolSize = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
        return Executors.newFixedThreadPool(poolSize);
    }

    @Bean(name = "insertExecutor", destroyMethod = "shutdown")
    public ExecutorService insertExecutor() {
        int poolSize = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
        return Executors.newFixedThreadPool(poolSize);
    }


}
