package com.foo.test;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiscardOldestPolicyExtension extends ThreadPoolExecutor.DiscardOldestPolicy {

  final ThreadPoolTaskExecutor taskExecutor;

  public DiscardOldestPolicyExtension(ThreadPoolTaskExecutor taskExecutor) {
    this.taskExecutor = taskExecutor;
  }

  @Override
  public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
    log.warn("Executor {} rejected Task {}. Active count: {}. Completed count: {},  Scheduled count: {}. Queue size: {}", taskExecutor.getThreadNamePrefix(), r, 
        e.getActiveCount(), e.getCompletedTaskCount(), e.getTaskCount(), e.getQueue().size());
    super.rejectedExecution(r, e);
  }
}
