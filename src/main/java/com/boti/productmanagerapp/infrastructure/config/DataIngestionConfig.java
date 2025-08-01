package com.boti.productmanagerapp.infrastructure.config;

import com.boti.productmanagerapp.application.core.usecases.ProductIngestionUseCase;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class DataIngestionConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private LoggerPort log;

    @Autowired
    private ProductIngestionUseCase productIngestionUseCase;

    @Value("${app.ingestion.folder.location}")
    private String filePath;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        long startTime = System.currentTimeMillis();

        this.productIngestionUseCase.execute(this.filePath);

        long endTime = System.currentTimeMillis();
        log.info(DataIngestionConfig.class, String.format("Product data ingestion process completed in %d ms.", (endTime - startTime)));
    }
}