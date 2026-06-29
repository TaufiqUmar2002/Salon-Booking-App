package com.umar.service_offering.config;

import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    public Executor executor() {
        return  Executors.newFixedThreadPool(10);
    }
}
