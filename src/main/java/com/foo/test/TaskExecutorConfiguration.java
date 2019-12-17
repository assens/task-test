package com.foo.test;

import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskExecutorConfiguration {

  @Bean
  public TaskExecutorCustomizer taskExecutorCustomizer() {
    return taskExecutor -> {
      taskExecutor.setAllowCoreThreadTimeOut(true);
      taskExecutor.setRejectedExecutionHandler(new DiscardOldestPolicyExtension(taskExecutor));
    };
  }
}
