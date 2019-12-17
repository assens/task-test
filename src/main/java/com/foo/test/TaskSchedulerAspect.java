package com.foo.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Aspect
@Component
@Lazy(false)
public class TaskSchedulerAspect {

  @Setter
  private int maxQueueSize = 10;

  @Around("execution(* org.springframework.scheduling.TaskScheduler.schedule(..))")
  public Object beforeTaskScheduler(final ProceedingJoinPoint joinPoint) throws Throwable {
    final ThreadPoolTaskScheduler taskScheduler = (ThreadPoolTaskScheduler) joinPoint.getTarget();
    final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = taskScheduler.getScheduledThreadPoolExecutor();
    final BlockingQueue<Runnable> queue = scheduledThreadPoolExecutor.getQueue();
    int size = queue.size();
    if (size >= maxQueueSize) {
      throw new RejectedExecutionException("ScheduledThreadPoolExecutor queue is full. Unable to schedule task.");
    } else {
      return joinPoint.proceed();
    }
  }
}
