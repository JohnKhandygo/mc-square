package com.ksp.khandygo.performance;

import com.google.common.base.Stopwatch;
import com.ksp.khandygo.geometry.core.Region;
import com.ksp.khandygo.performance.utils.InputProvider;
import com.ksp.khandygo.performance.utils.SetupParametersFormatter;
import com.ksp.khandygo.performance.utils.StatisticsCollector;
import com.ksp.khandygo.processing.MCEstimator;
import com.ksp.khandygo.processing.parallel.ParallelMCEstimator;
import static java.util.concurrent.Executors.newCachedThreadPool;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ParallelMCEstimatorRunner {

  private static final StatisticsCollector MEASURES = StatisticsCollector.newOne();

  public static void main(final String[] args)
  throws Exception {
    final int parallelismLevel = Integer.parseInt(args[0]);
    final int mcSampleSize = Integer.parseInt(args[1]);
    final List<Region> inputs = InputProvider.feededFrom(args[2]).provide();
    SetupParametersFormatter.print(args[2], inputs.size(), mcSampleSize, parallelismLevel);

    final ExecutorService executor = newCachedThreadPool();
    for (Region input : inputs) {
      MCEstimator estimator = new ParallelMCEstimator(executor, parallelismLevel);

      final Stopwatch measurer = Stopwatch.createStarted();
      estimator.estimate(input, mcSampleSize);
      MEASURES.add(measurer.elapsed(TimeUnit.MILLISECONDS));
    }

    MEASURES.print();
    executor.shutdownNow();
  }
}