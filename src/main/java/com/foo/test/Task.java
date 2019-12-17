package com.foo.test;

import java.util.Random;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Task implements Runnable {
  final String name;
  final Random random = new Random();

  public Task(final String name) {
    this.name = name;
  }

  @Override
  public void run() {
    log.info("Task: {} Start", name);
    try {
      Thread.sleep(1000L + random.nextInt(1000));
    } catch (final InterruptedException e) {
      log.error("Task {} Interrupted", name);
      Thread.currentThread().interrupt();
    }
    log.info("Task {} End", name);
  }

  @Override
  public String toString() {
    return String.format("Task %s", name);
  }

}
