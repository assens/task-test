package com.foo.test;

import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Lazy(false)
@Slf4j
public class TaskExecutorService {

  @Autowired
  private TaskExecutor taskExecutor;

  @PostConstruct
  public void init() {
    taskExecutor.execute(() -> log.info("Initialized"));
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    IntStream.range(1, 50).forEach(i -> taskExecutor.execute(new Task(String.valueOf(i))));
  }
}
