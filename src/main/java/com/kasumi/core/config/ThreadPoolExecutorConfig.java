package com.kasumi.core.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author kasumi
 * @Description: 线程池配置类
 */
@Configuration
@Slf4j
public class ThreadPoolExecutorConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadFactory threadFactory = r -> {
            Thread thread = new Thread(r);
            log.info("线程:" + thread.getId());
            return thread;
        };

        return new ThreadPoolExecutor(2, 4, 1000, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4), threadFactory);
    }
}
