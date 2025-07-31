package com.boti.productmanagerapp.application.ports.out;

public interface LoggerPort {

    void info(Class<?> clazz, String message);
    void warn(Class<?> clazz, String message);
    void error(Class<?> clazz, String message, Throwable throwable);

}
