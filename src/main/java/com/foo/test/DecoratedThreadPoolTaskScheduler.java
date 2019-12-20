package com.foo.test;

import static java.util.Objects.isNull;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import lombok.Setter;

public class DecoratedThreadPoolTaskScheduler extends ThreadPoolTaskScheduler {

  private static final long serialVersionUID = 1L;

  @Nullable
  @Setter
  private transient TaskDecorator taskDecorator;

  private Runnable decorated(final Runnable task) {
    return isNull(taskDecorator) ? task : taskDecorator.decorate(task);
  }

  @Override
  public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
    return super.schedule(decorated(task), startTime);
  }

  @Override
  public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
    return super.schedule(decorated(task), trigger);
  }

  @Override
  public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
    return super.scheduleAtFixedRate(decorated(task), startTime, period);
  }

  @Override
  public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
    return super.scheduleAtFixedRate(decorated(task), period);
  }

  @Override
  public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
    return super.scheduleWithFixedDelay(decorated(task), startTime, delay);
  }

  @Override
  public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
    return super.scheduleWithFixedDelay(decorated(task), delay);
  }

  @Override
  public void execute(Runnable task) {
    super.execute(decorated(task));
  }

  @Override
  public void execute(Runnable task, long startTimeout) {
    super.execute(decorated(task), startTimeout);
  }

}
