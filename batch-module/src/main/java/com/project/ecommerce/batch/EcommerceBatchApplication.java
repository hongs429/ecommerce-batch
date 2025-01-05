package com.project.ecommerce.batch;


import io.prometheus.client.exporter.PushGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class EcommerceBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceBatchApplication.class, args);
    }


    @Bean
    public PushGateway pushgateway(
            @Value("${prometheus.pushgateway.url:localhost:9091}") String url) {
        return new PushGateway(url);
    }


    @Bean
    public TaskExecutor batchTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(128);
        executor.setMaxPoolSize(128);
        executor.setQueueCapacity(256);
        executor.setThreadNamePrefix("batch-executor-");
        executor.setAllowCoreThreadTimeOut(true);   // 유휴상태
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);
        return executor;
    }
}
