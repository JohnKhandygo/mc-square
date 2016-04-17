package com.ksp.khandygo.processing.parallel;

import com.ksp.khandygo.geometry.core.Region;
import com.ksp.khandygo.processing.MCEstimator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelMCEstimator implements MCEstimator {

  private final ExecutorService executor;

  private final int parallelismLevel;

  public ParallelMCEstimator(final ExecutorService executor, final int parallelismLevel) {
    this.executor = executor;
    this.parallelismLevel = parallelismLevel;
  }

  @Override
  public double estimate(final Region regionTiEstimate, final int sampleSize) {
    final AtomicInteger successfulProbes = new AtomicInteger(0);
    final CountDownLatch latch = new CountDownLatch(parallelismLevel);
    for (int i = 0; i < parallelismLevel; ++i) {
      CompletableFuture
          .supplyAsync(() -> doNProbes(regionTiEstimate, sampleSize / parallelismLevel), executor)
          .thenAcceptAsync(successfulProbes::addAndGet)
          .thenAcceptAsync(v -> latch.countDown());
    }
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return ((double) successfulProbes.get()) / sampleSize;
  }
}
