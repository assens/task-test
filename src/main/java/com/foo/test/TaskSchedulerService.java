package com.foo.test;

import java.time.Instant;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Lazy(false)
@Slf4j
public class TaskSchedulerService {

  @Autowired
  private TaskExecutor taskExecutor;

  @Autowired
  private TaskScheduler taskScheduler;

  @PostConstruct
  public void init() {
    taskExecutor.execute(() -> log.info("Initialized"));
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    final Instant startTime = Instant.now().plusSeconds(4);
    IntStream.range(1, 50).forEach(i -> {
      final Task task = new Task("sched-" + i);
      try {
        taskScheduler.schedule(task, startTime);
      } catch (RejectedExecutionException e) {
        log.error("Error: {} Task: {}", e.getMessage(), task);
      }
    });
  }
}
