package com.foo.test;

import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class TaskSchedulerConfiguration {

  @Bean
  public TaskSchedulerCustomizer taskSchedulerCustomizer() {
    return taskScheduler -> {
      taskScheduler.setErrorHandler(t -> log.info("Error while executing scheduled task: {}", t.getMessage(), t));
      taskScheduler.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
    };
  }
}
