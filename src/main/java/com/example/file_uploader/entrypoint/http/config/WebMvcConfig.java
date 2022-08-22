package com.example.file_uploader.entrypoint.http.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAsync
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public ThreadPoolTaskExecutor mvcTaskExecutor() {
        final var taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return taskExecutor;
    }

    @Override
    public void configureAsyncSupport(final AsyncSupportConfigurer asyncSupportConfigurer) {
        asyncSupportConfigurer.setTaskExecutor(mvcTaskExecutor());
    }

}
