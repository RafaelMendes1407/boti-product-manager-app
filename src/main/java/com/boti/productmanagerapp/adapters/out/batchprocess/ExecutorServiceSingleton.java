package com.boti.productmanagerapp.adapters.out.batchprocess;


import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ExecutorServiceSingleton {

    private ExecutorService executor;

    private static ExecutorServiceSingleton instance;

    public ExecutorServiceSingleton() {
    }

    public static ExecutorService getInstance() {
        if (Objects.isNull(instance) || Objects.isNull(instance.executor)) {
            instance = new ExecutorServiceSingleton();
            instance.executor = Executors.newFixedThreadPool(ExecutorConfig.getIngestionPoolSize());
            return instance.executor;
        }
        return instance.executor;
    }

    public static void shutdown() {
        if (Objects.isNull(instance) || Objects.isNull(instance.executor)) return;
        instance.executor.shutdown();
        instance.executor = null;
    }


}
