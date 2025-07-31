package com.boti.productmanagerapp.infrastructure;

import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerAdapter implements LoggerPort {

    @Override
    public void info(Class<?> clazz, String message) {
        Logger log = LoggerFactory.getLogger(clazz);
        log.info(message);
    }

    @Override
    public void warn(Class<?> clazz, String message) {
        Logger log = LoggerFactory.getLogger(clazz);
        log.warn(message);
    }

    @Override
    public void error(Class<?> clazz, String message, Throwable throwable) {
        Logger log = LoggerFactory.getLogger(clazz);
        log.error(message, throwable);
    }
}
