package com.foo.test;

import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Lazy(false)
@Slf4j
public class TaskSchedulerConfiguration {

  @Bean
  public TaskScheduler taskScheduler() {
    final DecoratedThreadPoolTaskScheduler taskScheduler = new DecoratedThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(5);
    taskScheduler.setThreadNamePrefix("cust-task-sched-");
    final TaskDecorator taskDecorator = task -> () -> {
      final StopWatch stopWatch = new StopWatch(task.toString());
      stopWatch.start();
      task.run();
      stopWatch.stop();
      log.info("Task: {} took {} ms.", task.toString(), stopWatch.getTotalTimeMillis());
    };
    taskScheduler.setTaskDecorator(taskDecorator);
    return taskScheduler;
  }

  @Bean
  public TaskSchedulerCustomizer taskSchedulerCustomizer() {
    return taskScheduler -> {
      taskScheduler.setErrorHandler(t -> log.info("Error while executing scheduled task: {}", t.getMessage(), t));
      taskScheduler.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
    };
  }
}
