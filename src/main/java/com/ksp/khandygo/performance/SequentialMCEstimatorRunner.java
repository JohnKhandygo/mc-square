package com.ksp.khandygo.performance;

import com.google.common.base.Stopwatch;
import com.ksp.khandygo.geometry.core.Region;
import com.ksp.khandygo.performance.utils.InputProvider;
import com.ksp.khandygo.performance.utils.SetupParametersFormatter;
import com.ksp.khandygo.performance.utils.StatisticsCollector;
import com.ksp.khandygo.processing.MCEstimator;
import com.ksp.khandygo.processing.sequential.SequentialMCEstimator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SequentialMCEstimatorRunner {

  private static final StatisticsCollector MEASURES = StatisticsCollector.newOne();

  public static void main(final String[] args)
  throws Exception {
    final int mcSampleSize = Integer.parseInt(args[0]);
    final List<Region> inputs = InputProvider.feededFrom(args[1]).provide();

    SetupParametersFormatter.print(args[1], inputs.size(), mcSampleSize);

    for (Region input : inputs) {
      MCEstimator estimator = new SequentialMCEstimator();

      final Stopwatch measurer = Stopwatch.createStarted();
      estimator.estimate(input, mcSampleSize);
      MEASURES.add(measurer.elapsed(TimeUnit.MILLISECONDS));
    }

    MEASURES.print();
  }
}